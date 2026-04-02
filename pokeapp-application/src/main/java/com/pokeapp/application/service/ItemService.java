package com.pokeapp.application.service;

import com.pokeapp.application.dto.ItemDto;
import com.pokeapp.application.dto.PagedResponse;
import com.pokeapp.domain.item.Item;
import com.pokeapp.application.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public PagedResponse<ItemDto> findAll(String search, Pageable pageable) {
        return PagedResponse.from(
                itemRepository.findBySearch(search, pageable).map(this::toDto));
    }

    @Transactional(readOnly = true)
    public Optional<ItemDto> findById(Integer id) {
        return itemRepository.findById(id).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<ItemDto> findByName(String name) {
        return itemRepository.findByName(name).map(this::toDto);
    }

    private ItemDto toDto(Item i) {
        return new ItemDto(i.getId(), i.getName(), i.getEffectText(), i.getSpriteUrl());
    }
}