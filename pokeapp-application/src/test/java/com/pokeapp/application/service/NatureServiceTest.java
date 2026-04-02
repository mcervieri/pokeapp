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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
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

        when(natureRepository.findBySearch(isNull(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(adamant)));

        var result = natureService.findAll(null, Pageable.unpaged());

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0).name()).isEqualTo("adamant");
        assertThat(result.content().get(0).increasedStatName()).isEqualTo("attack");
        assertThat(result.content().get(0).decreasedStatName()).isEqualTo("special-attack");
    }

    @Test
    void findAll_neutralNature_returnsNullStats() {
        Nature hardy = new Nature(1, "hardy", null, null);

        when(natureRepository.findBySearch(isNull(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(hardy)));

        var result = natureService.findAll(null, Pageable.unpaged());

        assertThat(result.content().get(0).increasedStatName()).isNull();
        assertThat(result.content().get(0).decreasedStatName()).isNull();
    }
}