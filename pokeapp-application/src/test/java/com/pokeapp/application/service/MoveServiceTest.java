package com.pokeapp.application.service;

import com.pokeapp.application.dto.MoveDto;
import com.pokeapp.application.repository.MoveRepository;
import com.pokeapp.domain.move.DamageClass;
import com.pokeapp.domain.move.Move;
import com.pokeapp.domain.pokemon.Type;
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
class MoveServiceTest {

    @Mock
    private MoveRepository moveRepository;

    @InjectMocks
    private MoveService moveService;

    private Move buildFlamethrower() {
        Type fire = new Type(10, "fire");
        DamageClass special = new DamageClass("special");
        return new Move(53, "flamethrower", fire, special, 90, 100, 15, 0, "Burns the target.");
    }

    @Test
    void findAll_returnsMappedDtos() {
        when(moveRepository.findBySearch(isNull(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(buildFlamethrower())));

        var result = moveService.findAll(null, Pageable.unpaged());

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0).name()).isEqualTo("flamethrower");
        assertThat(result.content().get(0).type()).isEqualTo("fire");
        assertThat(result.content().get(0).damageClass()).isEqualTo("special");
        assertThat(result.content().get(0).power()).isEqualTo(90);
    }

    @Test
    void findAll_nullTypeAndClass_doesNotThrow() {
        Move splash = new Move(1, "splash", null, null, null, null, 40, 0, "Does nothing.");

        when(moveRepository.findBySearch(isNull(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(splash)));

        var result = moveService.findAll(null, Pageable.unpaged());

        assertThat(result.content().get(0).type()).isNull();
        assertThat(result.content().get(0).damageClass()).isNull();
        assertThat(result.content().get(0).power()).isNull();
    }

    @Test
    void findById_whenNotExists_returnsEmpty() {
        when(moveRepository.findById(99)).thenReturn(Optional.empty());

        assertThat(moveService.findById(99)).isEmpty();
    }
}