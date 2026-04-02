package com.pokeapp.application.service;

import com.pokeapp.application.dto.TypeDto;
import com.pokeapp.application.repository.TypeEffectivenessRepository;
import com.pokeapp.application.repository.TypeRepository;
import com.pokeapp.domain.pokemon.Type;
import com.pokeapp.domain.pokemon.TypeEffectiveness;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TypeServiceTest {

    @Mock
    private TypeRepository typeRepository;

    @Mock
    private TypeEffectivenessRepository typeEffectivenessRepository;

    @InjectMocks
    private TypeService typeService;

    @Test
    void findAll_returnsMappedDtos() {
        Type fire = new Type(10, "fire");
        Type water = new Type(11, "water");
        TypeEffectiveness matchup = new TypeEffectiveness(fire, water, new BigDecimal("0.50"));

        when(typeRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(fire)));
        when(typeEffectivenessRepository.findByAttackingTypeId(fire.getId()))
                .thenReturn(List.of(matchup));

        var result = typeService.findAll(Pageable.unpaged());

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0).name()).isEqualTo("fire");
        assertThat(result.content().get(0).damageRelations()).hasSize(1);
        assertThat(result.content().get(0).damageRelations().get(0).targetType()).isEqualTo("water");
        assertThat(result.content().get(0).damageRelations().get(0).multiplier())
                .isEqualByComparingTo(new BigDecimal("0.50"));
    }

    @Test
    void findById_whenNotExists_returnsEmpty() {
        when(typeRepository.findById(99)).thenReturn(Optional.empty());

        assertThat(typeService.findById(99)).isEmpty();
    }
}