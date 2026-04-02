package com.pokeapp.web.controller;

import com.pokeapp.application.dto.PagedResponse;
import com.pokeapp.application.dto.PokemonDto;
import com.pokeapp.application.exception.ResourceNotFoundException;
import com.pokeapp.application.service.PokemonService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pokemon")
@RequiredArgsConstructor
public class PokemonController {

    private final PokemonService pokemonService;

    @GetMapping
    public ResponseEntity<PagedResponse<PokemonDto>> getAll(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String search,
            Pageable pageable) {
        return ResponseEntity.ok(pokemonService.findAll(type, search, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PokemonDto> getById(@PathVariable Integer id) {
        PokemonDto dto = pokemonService.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forPokemon(id));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PokemonDto> getByName(@PathVariable String name) {
        PokemonDto dto = pokemonService.findByName(name)
                .orElseThrow(() -> ResourceNotFoundException.forPokemon(name));
        return ResponseEntity.ok(dto);
    }
}