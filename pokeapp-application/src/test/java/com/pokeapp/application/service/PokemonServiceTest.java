package com.pokeapp.application.service;

import com.pokeapp.application.dto.PokemonDto;
import com.pokeapp.application.repository.PokemonRepository;
import com.pokeapp.domain.pokemon.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokemonServiceTest {

    @Mock
    private PokemonRepository pokemonRepository;

    @InjectMocks
    private PokemonService pokemonService;

    private Pokemon buildBulbasaur() {
        Type grass = new Type(1, "grass");
        Stat hp = new Stat(1, "hp");
        Pokemon p = new Pokemon(1, "bulbasaur", null, true, 7, 69, 64, null);
        PokemonType pt = new PokemonType(p, grass, 1);
        PokemonStat ps = new PokemonStat(p, hp, 45, 0);
        return buildPokemonWithCollections(p, List.of(pt), List.of(ps));
    }

    private Pokemon buildPokemonWithCollections(Pokemon p,
            List<PokemonType> types, List<PokemonStat> stats) {
        try {
            var typesField = Pokemon.class.getDeclaredField("pokemonTypes");
            typesField.setAccessible(true);
            typesField.set(p, types);

            var statsField = Pokemon.class.getDeclaredField("pokemonStats");
            statsField.setAccessible(true);
            statsField.set(p, stats);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set collections via reflection", e);
        }
        return p;
    }

    @Test
    void findAll_returnsMappedDtos() {
        when(pokemonRepository.findByFilters(isNull(), isNull(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(buildBulbasaur())));

        var result = pokemonService.findAll(null, null, Pageable.unpaged());

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0).name()).isEqualTo("bulbasaur");
        assertThat(result.content().get(0).types()).containsExactly("grass");
        assertThat(result.content().get(0).stats()).hasSize(1);
        assertThat(result.content().get(0).stats().get(0).baseValue()).isEqualTo(45);
    }

    @Test
    void findById_whenExists_returnsDto() {
        when(pokemonRepository.findById(1)).thenReturn(Optional.of(buildBulbasaur()));

        Optional<PokemonDto> result = pokemonService.findById(1);

        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo("bulbasaur");
    }

    @Test
    void findById_whenNotExists_returnsEmpty() {
        when(pokemonRepository.findById(99)).thenReturn(Optional.empty());

        assertThat(pokemonService.findById(99)).isEmpty();
    }

    @Test
    void findByName_whenExists_returnsDto() {
        when(pokemonRepository.findByName("bulbasaur"))
                .thenReturn(Optional.of(buildBulbasaur()));

        Optional<PokemonDto> result = pokemonService.findByName("bulbasaur");

        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo("bulbasaur");
    }
}