package com.pokeapp.application.service;

import com.pokeapp.application.dto.user.PokedexEntryDto;
import com.pokeapp.application.exception.ResourceNotFoundException;
import com.pokeapp.application.repository.PokemonRepository;
import com.pokeapp.application.repository.TrainerPokedexRepository;
import com.pokeapp.application.repository.TrainerRepository;
import com.pokeapp.domain.pokemon.Pokemon;
import com.pokeapp.domain.trainer.Trainer;
import com.pokeapp.domain.trainer.TrainerPokedex;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PokedexService {

    private final TrainerPokedexRepository pokedexRepository;
    private final TrainerRepository trainerRepository;
    private final PokemonRepository pokemonRepository;

    public PokedexService(TrainerPokedexRepository pokedexRepository,
            TrainerRepository trainerRepository,
            PokemonRepository pokemonRepository) {
        this.pokedexRepository = pokedexRepository;
        this.trainerRepository = trainerRepository;
        this.pokemonRepository = pokemonRepository;
    }

    @Transactional
    public PokedexEntryDto markSeen(String username, Integer pokemonId) {
        return updateEntry(username, pokemonId, entry -> entry.markSeen());
    }

    @Transactional
    public PokedexEntryDto markCaught(String username, Integer pokemonId) {
        return updateEntry(username, pokemonId, entry -> entry.markCaught());
    }

    @Transactional
    public PokedexEntryDto unmarkCaught(String username, Integer pokemonId) {
        return updateEntry(username, pokemonId, entry -> entry.unmarkCaught());
    }

    @Transactional(readOnly = true)
    public List<PokedexEntryDto> getCaught(String username) {
        Trainer trainer = resolveTrainer(username);
        return pokedexRepository.findByTrainerIdAndCaughtTrue(trainer.getId())
                .stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<PokedexEntryDto> getSeen(String username) {
        Trainer trainer = resolveTrainer(username);
        return pokedexRepository.findByTrainerIdAndSeenTrue(trainer.getId())
                .stream().map(this::toDto).toList();
    }

    private PokedexEntryDto updateEntry(String username, Integer pokemonId,
            java.util.function.Consumer<TrainerPokedex> action) {
        Trainer trainer = resolveTrainer(username);
        Pokemon pokemon = pokemonRepository.findById(pokemonId)
                .orElseThrow(() -> ResourceNotFoundException.forPokemon(pokemonId));

        TrainerPokedex entry = pokedexRepository
                .findByIdTrainerIdAndIdPokemonId(trainer.getId(), pokemon.getId())
                .orElse(new TrainerPokedex(trainer, pokemon));

        action.accept(entry);
        return toDto(pokedexRepository.save(entry));
    }

    private Trainer resolveTrainer(String username) {
        return trainerRepository.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.forTrainer(username));
    }

    private PokedexEntryDto toDto(TrainerPokedex e) {
        return new PokedexEntryDto(
                e.getPokemon().getId(),
                e.getPokemon().getName(),
                buildSpriteUrl(e.getPokemon()),
                e.getSeen(),
                e.getCaught(),
                e.getUpdatedAt());
    }

    private String buildSpriteUrl(Pokemon pokemon) {
        if (pokemon.getPokeapiId() == null)
            return null;
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
                + pokemon.getPokeapiId() + ".png";
    }
}