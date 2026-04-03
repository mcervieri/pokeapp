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
class FavoriteServiceTest {

    @Mock
    TrainerFavoriteRepository favoriteRepository;
    @Mock
    TrainerRepository trainerRepository;
    @Mock
    PokemonRepository pokemonRepository;

    @InjectMocks
    FavoriteService favoriteService;

    private Trainer trainer() {
        return new Trainer("ash");
    }

    private Pokemon pokemon() {
        return new Pokemon(1, "bulbasaur", null, true, 7, 69, 64, null);
    }

    @Test
    void addFavorite_savesWhenNotAlreadyFavorited() {
        Trainer trainer = trainer();
        Pokemon pokemon = pokemon();
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer));
        when(pokemonRepository.findById(1)).thenReturn(Optional.of(pokemon));
        when(favoriteRepository.existsById(any())).thenReturn(false);

        favoriteService.addFavorite("ash", 1);

        verify(favoriteRepository).save(any(TrainerFavorite.class));
    }

    @Test
    void addFavorite_skipsWhenAlreadyFavorited() {
        Trainer trainer = trainer();
        Pokemon pokemon = pokemon();
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer));
        when(pokemonRepository.findById(1)).thenReturn(Optional.of(pokemon));
        when(favoriteRepository.existsById(any())).thenReturn(true);

        favoriteService.addFavorite("ash", 1);

        verify(favoriteRepository, never()).save(any());
    }

    @Test
    void addFavorite_throwsWhenPokemonNotFound() {
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer()));
        when(pokemonRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> favoriteService.addFavorite("ash", 999))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void removeFavorite_deletesWhenExists() {
        Trainer trainer = trainer();
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer));
        when(favoriteRepository.existsById(any())).thenReturn(true);

        favoriteService.removeFavorite("ash", 1);

        verify(favoriteRepository).deleteById(any(TrainerFavoriteId.class));
    }

    @Test
    void removeFavorite_skipsWhenNotExists() {
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer()));
        when(favoriteRepository.existsById(any())).thenReturn(false);

        favoriteService.removeFavorite("ash", 1);

        verify(favoriteRepository, never()).deleteById(any());
    }

    @Test
    void getFavorites_returnsEmptyListWhenNone() {
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer()));
        when(favoriteRepository.findByTrainerId(any())).thenReturn(List.of());

        List<FavoriteDto> result = favoriteService.getFavorites("ash");

        assertThat(result).isEmpty();
    }
}