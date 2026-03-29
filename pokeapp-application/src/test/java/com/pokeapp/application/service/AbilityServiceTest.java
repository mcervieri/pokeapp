package com.pokeapp.application.service;

import com.pokeapp.application.dto.AbilityDto;
import com.pokeapp.application.repository.AbilityRepository;
import com.pokeapp.domain.pokemon.Ability;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbilityServiceTest {

    @Mock
    private AbilityRepository abilityRepository;

    @InjectMocks
    private AbilityService abilityService;

    @Test
    void findAll_returnsMappedDtos() {
        Ability intimidate = new Ability(22, "intimidate", "Lowers opponent's attack on entry.");

        when(abilityRepository.findAll()).thenReturn(List.of(intimidate));

        List<AbilityDto> result = abilityService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("intimidate");
        assertThat(result.get(0).effectText()).isEqualTo("Lowers opponent's attack on entry.");
    }

    @Test
    void findById_whenNotExists_returnsEmpty() {
        when(abilityRepository.findById(99)).thenReturn(Optional.empty());

        assertThat(abilityService.findById(99)).isEmpty();
    }
}