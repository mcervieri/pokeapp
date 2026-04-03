package com.pokeapp.application.service;

import com.pokeapp.application.dto.user.PokedexEntryDto;
import com.pokeapp.application.exception.ResourceNotFoundException;
import com.pokeapp.application.repository.PokemonRepository;
import com.pokeapp.application.repository.TrainerPokedexRepository;
import com.pokeapp.application.repository.TrainerRepository;
import com.pokeapp.domain.pokemon.Pokemon;
import com.pokeapp.domain.trainer.Trainer;
import com.pokeapp.domain.trainer.TrainerPokedex;
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
class PokedexServiceTest {

    @Mock
    TrainerPokedexRepository pokedexRepository;
    @Mock
    TrainerRepository trainerRepository;
    @Mock
    PokemonRepository pokemonRepository;

    @InjectMocks
    PokedexService pokedexService;

    private Trainer trainer() {
        return new Trainer("ash");
    }

    private Pokemon pokemon() {
        return new Pokemon(1, "bulbasaur", null, true, 7, 69, 64, null);
    }

    @Test
    void markSeen_createsNewEntryAndMarksSeen() {
        Trainer trainer = trainer();
        Pokemon pokemon = pokemon();
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer));
        when(pokemonRepository.findById(1)).thenReturn(Optional.of(pokemon));
        when(pokedexRepository.findByIdTrainerIdAndIdPokemonId(any(), any()))
                .thenReturn(Optional.empty());
        TrainerPokedex entry = new TrainerPokedex(trainer, pokemon);
        entry.markSeen();
        when(pokedexRepository.save(any())).thenReturn(entry);

        PokedexEntryDto result = pokedexService.markSeen("ash", 1);

        assertThat(result.seen()).isTrue();
        verify(pokedexRepository).save(any(TrainerPokedex.class));
    }

    @Test
    void markCaught_setsBothSeenAndCaught() {
        Trainer trainer = trainer();
        Pokemon pokemon = pokemon();
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer));
        when(pokemonRepository.findById(1)).thenReturn(Optional.of(pokemon));
        when(pokedexRepository.findByIdTrainerIdAndIdPokemonId(any(), any()))
                .thenReturn(Optional.empty());
        TrainerPokedex entry = new TrainerPokedex(trainer, pokemon);
        entry.markCaught();
        when(pokedexRepository.save(any())).thenReturn(entry);

        PokedexEntryDto result = pokedexService.markCaught("ash", 1);

        assertThat(result.seen()).isTrue();
        assertThat(result.caught()).isTrue();
    }

    @Test
    void markSeen_throwsWhenPokemonNotFound() {
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer()));
        when(pokemonRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pokedexService.markSeen("ash", 999))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getCaught_returnsEmptyWhenNone() {
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer()));
        when(pokedexRepository.findByTrainerIdAndCaughtTrue(any())).thenReturn(List.of());

        List<PokedexEntryDto> result = pokedexService.getCaught("ash");

        assertThat(result).isEmpty();
    }

    @Test
    void unmarkCaught_setsOnlyCaughtToFalse() {
        Trainer trainer = trainer();
        Pokemon pokemon = pokemon();
        TrainerPokedex entry = new TrainerPokedex(trainer, pokemon);
        entry.markCaught();
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer));
        when(pokemonRepository.findById(1)).thenReturn(Optional.of(pokemon));
        when(pokedexRepository.findByIdTrainerIdAndIdPokemonId(any(), any()))
                .thenReturn(Optional.of(entry));
        when(pokedexRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        PokedexEntryDto result = pokedexService.unmarkCaught("ash", 1);

        assertThat(result.caught()).isFalse();
        assertThat(result.seen()).isTrue();
    }
}