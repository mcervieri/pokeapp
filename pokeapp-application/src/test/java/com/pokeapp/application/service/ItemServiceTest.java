package com.pokeapp.application.service;

import com.pokeapp.application.dto.ItemDto;
import com.pokeapp.application.repository.ItemRepository;
import com.pokeapp.domain.item.Item;
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
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    void findAll_returnsMappedDtos() {
        Item choiceBand = new Item(1, "choice-band",
                "Boosts attack by 50% but locks into one move.",
                "https://example.com/choice-band.png");

        when(itemRepository.findBySearch(isNull(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(choiceBand)));

        var result = itemService.findAll(null, Pageable.unpaged());

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0).name()).isEqualTo("choice-band");
        assertThat(result.content().get(0).effectText())
                .isEqualTo("Boosts attack by 50% but locks into one move.");
    }

    @Test
    void findById_whenNotExists_returnsEmpty() {
        when(itemRepository.findById(99)).thenReturn(Optional.empty());

        assertThat(itemService.findById(99)).isEmpty();
    }
}