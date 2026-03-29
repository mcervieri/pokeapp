package com.pokeapp.application.service;

import com.pokeapp.application.dto.NatureDto;
import com.pokeapp.application.repository.NatureRepository;
import com.pokeapp.domain.pokemon.Nature;
import com.pokeapp.domain.pokemon.Stat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NatureServiceTest {

    @Mock
    private NatureRepository natureRepository;

    @InjectMocks
    private NatureService natureService;

    @Test
    void findAll_withStats_returnsMappedDtos() {
        Stat attack = new Stat(1, "attack");
        Stat spAtk = new Stat(2, "special-attack");
        Nature adamant = new Nature(3, "adamant", attack, spAtk);

        when(natureRepository.findAll()).thenReturn(List.of(adamant));

        List<NatureDto> result = natureService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("adamant");
        assertThat(result.get(0).increasedStatName()).isEqualTo("attack");
        assertThat(result.get(0).decreasedStatName()).isEqualTo("special-attack");
    }

    @Test
    void findAll_neutralNature_returnsNullStats() {
        Nature hardy = new Nature(1, "hardy", null, null);

        when(natureRepository.findAll()).thenReturn(List.of(hardy));

        List<NatureDto> result = natureService.findAll();

        assertThat(result.get(0).increasedStatName()).isNull();
        assertThat(result.get(0).decreasedStatName()).isNull();
    }
}