package com.pokeapp.application.service;

import com.pokeapp.application.dto.user.RatingDto;
import com.pokeapp.application.exception.ResourceNotFoundException;
import com.pokeapp.application.repository.PokemonRepository;
import com.pokeapp.application.repository.TrainerRatingRepository;
import com.pokeapp.application.repository.TrainerRepository;
import com.pokeapp.domain.pokemon.Pokemon;
import com.pokeapp.domain.trainer.Trainer;
import com.pokeapp.domain.trainer.TrainerRating;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RatingService {

    private final TrainerRatingRepository ratingRepository;
    private final TrainerRepository trainerRepository;
    private final PokemonRepository pokemonRepository;

    public RatingService(TrainerRatingRepository ratingRepository,
            TrainerRepository trainerRepository,
            PokemonRepository pokemonRepository) {
        this.ratingRepository = ratingRepository;
        this.trainerRepository = trainerRepository;
        this.pokemonRepository = pokemonRepository;
    }

    @Transactional
    public RatingDto rateOrUpdate(String username, Integer pokemonId, Integer score) {
        Trainer trainer = resolveTrainer(username);
        Pokemon pokemon = pokemonRepository.findById(pokemonId)
                .orElseThrow(() -> ResourceNotFoundException.forPokemon(pokemonId));

        TrainerRating rating = ratingRepository
                .findByIdTrainerIdAndIdPokemonId(trainer.getId(), pokemon.getId())
                .orElse(null);

        if (rating == null) {
            rating = new TrainerRating(trainer, pokemon, score);
        } else {
            rating.updateScore(score);
        }

        return toDto(ratingRepository.save(rating));
    }

    @Transactional(readOnly = true)
    public Double getAverageRating(Integer pokemonId) {
        return ratingRepository.findAverageScoreByPokemonId(pokemonId);
    }

    @Transactional
    public void deleteRating(String username, Integer pokemonId) {
        Trainer trainer = resolveTrainer(username);
        ratingRepository.findByIdTrainerIdAndIdPokemonId(trainer.getId(), pokemonId)
                .ifPresent(ratingRepository::delete);
    }

    private Trainer resolveTrainer(String username) {
        return trainerRepository.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.forTrainer(username));
    }

    private RatingDto toDto(TrainerRating r) {
        return new RatingDto(
                r.getPokemon().getId(),
                r.getTrainer().getUsername(),
                r.getScore(),
                r.getUpdatedAt());
    }
}