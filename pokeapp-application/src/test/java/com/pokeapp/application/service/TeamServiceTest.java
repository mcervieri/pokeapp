package com.pokeapp.application.service;

import com.pokeapp.application.dto.team.*;
import com.pokeapp.application.exception.ResourceNotFoundException;
import com.pokeapp.application.repository.*;
import com.pokeapp.domain.pokemon.Pokemon;
import com.pokeapp.domain.trainer.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    TeamRepository teamRepository;
    @Mock
    TeamSlotRepository slotRepository;
    @Mock
    TeamSlotMoveRepository slotMoveRepository;
    @Mock
    TeamSlotEvRepository slotEvRepository;
    @Mock
    TeamSlotIvRepository slotIvRepository;
    @Mock
    TrainerRepository trainerRepository;
    @Mock
    PokemonRepository pokemonRepository;
    @Mock
    AbilityRepository abilityRepository;
    @Mock
    NatureRepository natureRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    MoveRepository moveRepository;
    @Mock
    StatRepository statRepository;

    @InjectMocks
    TeamService teamService;

    @Test
    void getMyTeams_createsTrainerIfNotExists() {
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.empty());
        Trainer trainer = new Trainer("ash");
        when(trainerRepository.save(any())).thenReturn(trainer);
        when(teamRepository.findByTrainerId(any())).thenReturn(List.of());

        List<TeamSummaryDto> result = teamService.getMyTeams("ash");

        assertThat(result).isEmpty();
        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void getMyTeams_returnsTeamsForExistingTrainer() {
        Trainer trainer = new Trainer("ash");
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer));
        Team team = new Team(trainer, "My Team", "OU");
        when(teamRepository.findByTrainerId(any())).thenReturn(List.of(team));
        when(slotRepository.findByTeamIdOrderByPosition(any())).thenReturn(List.of());

        List<TeamSummaryDto> result = teamService.getMyTeams("ash");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("My Team");
        assertThat(result.get(0).format()).isEqualTo("OU");
    }

    @Test
    void createTeam_savesAndReturnsDetail() {
        Trainer trainer = new Trainer("ash");
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer));
        when(teamRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(slotRepository.findByTeamIdOrderByPosition(any())).thenReturn(List.of());

        TeamDetailDto result = teamService.createTeam(
                new CreateTeamRequest("Dream Team", "VGC"), "ash");

        assertThat(result.name()).isEqualTo("Dream Team");
        assertThat(result.format()).isEqualTo("VGC");
        assertThat(result.slots()).isEmpty();
        verify(teamRepository).save(any(Team.class));
    }

    @Test
    void getTeamById_throwsIfNotOwned() {
        Trainer other = new Trainer("misty");
        Team team = new Team(other, "Misty's Team", "OU");
        when(teamRepository.findById(1)).thenReturn(Optional.of(team));

        assertThatThrownBy(() -> teamService.getTeamById(1, "ash"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteTeam_throwsIfNotFound() {
        when(teamRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teamService.deleteTeam(99, "ash"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void upsertSlot_throwsIfInvalidPosition() {
        Trainer trainer = new Trainer("ash");
        Team team = new Team(trainer, "My Team", "OU");
        when(teamRepository.findById(1)).thenReturn(Optional.of(team));

        assertThatThrownBy(() -> teamService.upsertSlot(
                1, 7, new UpsertSlotRequest(1, null, null, null, null, null, null, null), "ash"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Position must be between 1 and 6");
    }

    @Test
    void upsertSlot_throwsIfPokemonNotFound() {
        Trainer trainer = new Trainer("ash");
        Team team = new Team(trainer, "My Team", "OU");
        when(teamRepository.findById(1)).thenReturn(Optional.of(team));
        when(slotRepository.findByTeamIdAndPosition(1, 1)).thenReturn(Optional.empty());
        when(pokemonRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teamService.upsertSlot(
                1, 1, new UpsertSlotRequest(999, null, null, null, null, null, null, null), "ash"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}