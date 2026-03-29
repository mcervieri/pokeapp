package com.pokeapp.application.service;

import com.pokeapp.application.dto.ItemDto;
import com.pokeapp.domain.item.Item;
import com.pokeapp.application.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public List<ItemDto> findAll() {
        return itemRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<ItemDto> findById(Integer id) {
        return itemRepository.findById(id)
                .map(this::toDto);
    }

    private ItemDto toDto(Item i) {
        return new ItemDto(
                i.getId(),
                i.getName(),
                i.getEffectText(),
                i.getSpriteUrl());
    }
}