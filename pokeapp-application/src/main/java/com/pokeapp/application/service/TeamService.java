package com.pokeapp.application.service;

import com.pokeapp.application.dto.team.*;
import com.pokeapp.application.repository.*;
import com.pokeapp.domain.item.Item;
import com.pokeapp.domain.pokemon.*;
import com.pokeapp.domain.trainer.*;
import com.pokeapp.application.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pokeapp.domain.move.Move;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamSlotRepository slotRepository;
    private final TeamSlotMoveRepository slotMoveRepository;
    private final TeamSlotEvRepository slotEvRepository;
    private final TeamSlotIvRepository slotIvRepository;
    private final TrainerRepository trainerRepository;
    private final PokemonRepository pokemonRepository;
    private final AbilityRepository abilityRepository;
    private final NatureRepository natureRepository;
    private final ItemRepository itemRepository;
    private final MoveRepository moveRepository;
    private final StatRepository statRepository;

    public TeamService(
            TeamRepository teamRepository,
            TeamSlotRepository slotRepository,
            TeamSlotMoveRepository slotMoveRepository,
            TeamSlotEvRepository slotEvRepository,
            TeamSlotIvRepository slotIvRepository,
            TrainerRepository trainerRepository,
            PokemonRepository pokemonRepository,
            AbilityRepository abilityRepository,
            NatureRepository natureRepository,
            ItemRepository itemRepository,
            MoveRepository moveRepository,
            StatRepository statRepository) {
        this.teamRepository = teamRepository;
        this.slotRepository = slotRepository;
        this.slotMoveRepository = slotMoveRepository;
        this.slotEvRepository = slotEvRepository;
        this.slotIvRepository = slotIvRepository;
        this.trainerRepository = trainerRepository;
        this.pokemonRepository = pokemonRepository;
        this.abilityRepository = abilityRepository;
        this.natureRepository = natureRepository;
        this.itemRepository = itemRepository;
        this.moveRepository = moveRepository;
        this.statRepository = statRepository;
    }

    // ─── Team CRUD ────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<TeamSummaryDto> getMyTeams(String username) {
        Trainer trainer = findOrCreateTrainer(username);
        List<Team> teams = teamRepository.findByTrainerId(trainer.getId());
        return teams.stream().map(this::toSummary).toList();
    }

    @Transactional(readOnly = true)
    public TeamDetailDto getTeamById(Integer teamId, String username) {
        Team team = requireOwnedTeam(teamId, username);
        return toDetail(team);
    }

    public TeamDetailDto createTeam(CreateTeamRequest request, String username) {
        Trainer trainer = findOrCreateTrainer(username);
        Team team = new Team(trainer, request.name(), request.format());
        teamRepository.save(team);
        return toDetail(team);
    }

    public TeamDetailDto updateTeam(Integer teamId, UpdateTeamRequest request, String username) {
        Team team = requireOwnedTeam(teamId, username);
        team.update(request.name(), request.format());
        return toDetail(team);
    }

    public void deleteTeam(Integer teamId, String username) {
        Team team = requireOwnedTeam(teamId, username);
        teamRepository.delete(team);
    }

    // ─── Slot operations ──────────────────────────────────────

    public TeamSlotDto upsertSlot(Integer teamId, Integer position, UpsertSlotRequest request, String username) {
        Team team = requireOwnedTeam(teamId, username);

        if (position < 1 || position > 6) {
            throw new IllegalArgumentException("Position must be between 1 and 6");
        }

        // Delete existing slot at this position if present
        slotRepository.findByTeamIdAndPosition(teamId, position)
                .ifPresent(existing -> {
                    slotMoveRepository.deleteAll(slotMoveRepository.findByTeamSlotIdOrderByIdSlot(existing.getId()));
                    slotEvRepository.deleteAll(slotEvRepository.findByTeamSlotId(existing.getId()));
                    slotIvRepository.deleteAll(slotIvRepository.findByTeamSlotId(existing.getId()));
                    slotRepository.delete(existing);
                    slotRepository.flush();
                });

        Pokemon pokemon = pokemonRepository.findById(request.pokemonId())
                .orElseThrow(() -> new ResourceNotFoundException("Pokemon not found: " + request.pokemonId()));

        TeamSlot slot = new TeamSlot(team, position, pokemon);

        if (request.abilityId() != null) {
            Ability ability = abilityRepository.findById(request.abilityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ability not found: " + request.abilityId()));
            slot.setAbility(ability);
        }

        if (request.natureId() != null) {
            Nature nature = natureRepository.findById(request.natureId())
                    .orElseThrow(() -> new ResourceNotFoundException("Nature not found: " + request.natureId()));
            slot.setNature(nature);
        }

        if (request.itemId() != null) {
            Item item = itemRepository.findById(request.itemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item not found: " + request.itemId()));
            slot.setItem(item);
        }

        if (request.nickname() != null) {
            slot.setNickname(request.nickname());
        }

        slotRepository.save(slot);
        slotRepository.flush(); // slot.getId() must be populated before creating EVs/IVs/moves

        // Moves
        if (request.moveIds() != null) {
            if (request.moveIds().size() > 4) {
                throw new IllegalArgumentException("A slot can have at most 4 moves");
            }
            for (int i = 0; i < request.moveIds().size(); i++) {
                Move move = moveRepository.findById(request.moveIds().get(i))
                        .orElseThrow(() -> new ResourceNotFoundException("Move not found"));
                slotMoveRepository.save(new TeamSlotMove(slot, move, i + 1));
            }
        }

        // EVs
        if (request.evs() != null) {
            int total = request.evs().values().stream().mapToInt(Integer::intValue).sum();
            if (total > 510) {
                throw new IllegalArgumentException("Total EVs cannot exceed 510");
            }
            for (Map.Entry<Integer, Integer> entry : request.evs().entrySet()) {
                Stat stat = statRepository.findById(entry.getKey())
                        .orElseThrow(() -> new ResourceNotFoundException("Stat not found: " + entry.getKey()));
                slotEvRepository.save(new TeamSlotEv(slot, stat, entry.getValue()));
            }
        }

        // IVs
        if (request.ivs() != null) {
            for (Map.Entry<Integer, Integer> entry : request.ivs().entrySet()) {
                Stat stat = statRepository.findById(entry.getKey())
                        .orElseThrow(() -> new ResourceNotFoundException("Stat not found: " + entry.getKey()));
                slotIvRepository.save(new TeamSlotIv(slot, stat, entry.getValue()));
            }
        }

        team.touch();
        return toSlotDto(slot);
    }

    public void deleteSlot(Integer teamId, Integer position, String username) {
        requireOwnedTeam(teamId, username);
        TeamSlot slot = slotRepository.findByTeamIdAndPosition(teamId, position)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found at position " + position));
        slotMoveRepository.deleteAll(slotMoveRepository.findByTeamSlotIdOrderByIdSlot(slot.getId()));
        slotEvRepository.deleteAll(slotEvRepository.findByTeamSlotId(slot.getId()));
        slotIvRepository.deleteAll(slotIvRepository.findByTeamSlotId(slot.getId()));
        slotRepository.delete(slot);
    }

    // ─── Helpers ──────────────────────────────────────────────

    private Trainer findOrCreateTrainer(String username) {
        return trainerRepository.findByUsername(username)
                .orElseGet(() -> trainerRepository.save(new Trainer(username)));
    }

    private Team requireOwnedTeam(Integer teamId, String username) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found: " + teamId));
        if (!team.getTrainer().getUsername().equals(username)) {
            throw new ResourceNotFoundException("Team not found: " + teamId);
        }
        return team;
    }

    // ─── Mappers ──────────────────────────────────────────────

    private TeamSummaryDto toSummary(Team team) {
        int slotCount = slotRepository.findByTeamIdOrderByPosition(team.getId()).size();
        return new TeamSummaryDto(
                team.getId(),
                team.getName(),
                team.getFormat(),
                slotCount,
                team.getCreatedAt(),
                team.getUpdatedAt());
    }

    private TeamDetailDto toDetail(Team team) {
        List<TeamSlot> slots = slotRepository.findByTeamIdOrderByPosition(team.getId());
        List<TeamSlotDto> slotDtos = slots.stream().map(this::toSlotDto).toList();
        return new TeamDetailDto(
                team.getId(),
                team.getName(),
                team.getFormat(),
                team.getCreatedAt(),
                team.getUpdatedAt(),
                slotDtos);
    }

    private TeamSlotDto toSlotDto(TeamSlot slot) {
        List<String> moves = slotMoveRepository
                .findByTeamSlotIdOrderByIdSlot(slot.getId()).stream()
                .map(m -> m.getMove().getName())
                .toList();

        Map<String, Integer> evs = slotEvRepository
                .findByTeamSlotId(slot.getId()).stream()
                .collect(Collectors.toMap(
                        e -> e.getStat().getName(),
                        TeamSlotEv::getValue));

        Map<String, Integer> ivs = slotIvRepository
                .findByTeamSlotId(slot.getId()).stream()
                .collect(Collectors.toMap(
                        i -> i.getStat().getName(),
                        TeamSlotIv::getValue));

        String spriteUrl = slot.getPokemon().getPokeapiId() != null
                ? "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
                        + slot.getPokemon().getPokeapiId() + ".png"
                : null;

        return new TeamSlotDto(
                slot.getId(),
                slot.getPosition(),
                slot.getPokemon().getId(),
                slot.getPokemon().getName(),
                spriteUrl,
                slot.getAbility() != null ? slot.getAbility().getName() : null,
                slot.getNature() != null ? slot.getNature().getName() : null,
                slot.getItem() != null ? slot.getItem().getName() : null,
                slot.getNickname(),
                moves,
                evs,
                ivs);
    }
}