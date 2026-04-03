package com.pokeapp.web.controller;

import com.pokeapp.application.dto.ItemDto;
import com.pokeapp.application.dto.PagedResponse;
import com.pokeapp.application.exception.ResourceNotFoundException;
import com.pokeapp.application.service.ItemService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/items")
@Tag(name = "Items", description = "Held items and their effects")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<PagedResponse<ItemDto>> getAll(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        return ResponseEntity.ok(itemService.findAll(search, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getById(@PathVariable Integer id) {
        ItemDto dto = itemService.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forItem(id));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ItemDto> getByName(@PathVariable String name) {
        ItemDto dto = itemService.findByName(name)
                .orElseThrow(() -> ResourceNotFoundException.forItem(name));
        return ResponseEntity.ok(dto);
    }
}