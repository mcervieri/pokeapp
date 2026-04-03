package com.pokeapp.application.service;

import com.pokeapp.application.dto.user.RatingDto;
import com.pokeapp.application.exception.ResourceNotFoundException;
import com.pokeapp.application.repository.PokemonRepository;
import com.pokeapp.application.repository.TrainerRatingRepository;
import com.pokeapp.application.repository.TrainerRepository;
import com.pokeapp.domain.pokemon.Pokemon;
import com.pokeapp.domain.trainer.Trainer;
import com.pokeapp.domain.trainer.TrainerRating;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    TrainerRatingRepository ratingRepository;
    @Mock
    TrainerRepository trainerRepository;
    @Mock
    PokemonRepository pokemonRepository;

    @InjectMocks
    RatingService ratingService;

    private Trainer trainer() {
        return new Trainer("ash");
    }

    private Pokemon pokemon() {
        return new Pokemon(1, "bulbasaur", null, true, 7, 69, 64, null);
    }

    @Test
    void rateOrUpdate_createsNewRating() {
        Trainer trainer = trainer();
        Pokemon pokemon = pokemon();
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer));
        when(pokemonRepository.findById(1)).thenReturn(Optional.of(pokemon));
        when(ratingRepository.findByIdTrainerIdAndIdPokemonId(any(), any()))
                .thenReturn(Optional.empty());
        TrainerRating saved = new TrainerRating(trainer, pokemon, 5);
        when(ratingRepository.save(any())).thenReturn(saved);

        RatingDto result = ratingService.rateOrUpdate("ash", 1, 5);

        assertThat(result.score()).isEqualTo(5);
        assertThat(result.username()).isEqualTo("ash");
        verify(ratingRepository).save(any(TrainerRating.class));
    }

    @Test
    void rateOrUpdate_updatesExistingRating() {
        Trainer trainer = trainer();
        Pokemon pokemon = pokemon();
        TrainerRating existing = new TrainerRating(trainer, pokemon, 3);
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer));
        when(pokemonRepository.findById(1)).thenReturn(Optional.of(pokemon));
        when(ratingRepository.findByIdTrainerIdAndIdPokemonId(any(), any()))
                .thenReturn(Optional.of(existing));
        when(ratingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        RatingDto result = ratingService.rateOrUpdate("ash", 1, 5);

        assertThat(result.score()).isEqualTo(5);
    }

    @Test
    void rateOrUpdate_throwsWhenPokemonNotFound() {
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer()));
        when(pokemonRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ratingService.rateOrUpdate("ash", 999, 4))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getAverageRating_returnsNullWhenNoRatings() {
        when(ratingRepository.findAverageScoreByPokemonId(1)).thenReturn(null);

        Double avg = ratingService.getAverageRating(1);

        assertThat(avg).isNull();
    }

    @Test
    void deleteRating_deletesWhenExists() {
        Trainer trainer = trainer();
        Pokemon pokemon = pokemon();
        TrainerRating rating = new TrainerRating(trainer, pokemon, 4);
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer));
        when(ratingRepository.findByIdTrainerIdAndIdPokemonId(any(), any()))
                .thenReturn(Optional.of(rating));

        ratingService.deleteRating("ash", 1);

        verify(ratingRepository).delete(rating);
    }
}