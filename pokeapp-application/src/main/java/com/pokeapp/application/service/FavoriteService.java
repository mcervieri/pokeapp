package com.pokeapp.application.service;

import com.pokeapp.application.dto.user.FavoriteDto;
import com.pokeapp.application.exception.ResourceNotFoundException;
import com.pokeapp.application.repository.PokemonRepository;
import com.pokeapp.application.repository.TrainerFavoriteRepository;
import com.pokeapp.application.repository.TrainerRepository;
import com.pokeapp.domain.pokemon.Pokemon;
import com.pokeapp.domain.trainer.Trainer;
import com.pokeapp.domain.trainer.TrainerFavorite;
import com.pokeapp.domain.trainer.TrainerFavoriteId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {

    private final TrainerFavoriteRepository favoriteRepository;
    private final TrainerRepository trainerRepository;
    private final PokemonRepository pokemonRepository;

    public FavoriteService(TrainerFavoriteRepository favoriteRepository,
            TrainerRepository trainerRepository,
            PokemonRepository pokemonRepository) {
        this.favoriteRepository = favoriteRepository;
        this.trainerRepository = trainerRepository;
        this.pokemonRepository = pokemonRepository;
    }

    @Transactional
    public void addFavorite(String username, Integer pokemonId) {
        Trainer trainer = resolveTrainer(username);
        Pokemon pokemon = pokemonRepository.findById(pokemonId)
                .orElseThrow(() -> ResourceNotFoundException.forPokemon(pokemonId));
        TrainerFavoriteId id = new TrainerFavoriteId(trainer.getId(), pokemon.getId());
        if (!favoriteRepository.existsById(id)) {
            favoriteRepository.save(new TrainerFavorite(trainer, pokemon));
        }
    }

    @Transactional
    public void removeFavorite(String username, Integer pokemonId) {
        Trainer trainer = resolveTrainer(username);
        TrainerFavoriteId id = new TrainerFavoriteId(trainer.getId(), pokemonId);
        if (favoriteRepository.existsById(id)) {
            favoriteRepository.deleteById(id);
        }
    }

    @Transactional(readOnly = true)
    public List<FavoriteDto> getFavorites(String username) {
        Trainer trainer = resolveTrainer(username);
        return favoriteRepository.findByTrainerId(trainer.getId()).stream()
                .map(f -> new FavoriteDto(
                        f.getPokemon().getId(),
                        f.getPokemon().getName(),
                        buildSpriteUrl(f.getPokemon()),
                        f.getCreatedAt()))
                .toList();
    }

    private Trainer resolveTrainer(String username) {
        return trainerRepository.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.forTrainer(username));
    }

    private String buildSpriteUrl(Pokemon pokemon) {
        if (pokemon.getPokeapiId() == null)
            return null;
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
                + pokemon.getPokeapiId() + ".png";
    }
}