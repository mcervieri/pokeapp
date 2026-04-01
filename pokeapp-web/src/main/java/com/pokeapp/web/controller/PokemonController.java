package com.pokeapp.web.controller;

import com.pokeapp.application.dto.PokemonDto;
import com.pokeapp.application.service.PokemonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pokemon")
@RequiredArgsConstructor
public class PokemonController {

    private final PokemonService pokemonService;

    @GetMapping
    public ResponseEntity<List<PokemonDto>> getAll() {
        return ResponseEntity.ok(pokemonService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PokemonDto> getById(@PathVariable Integer id) {
        return pokemonService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PokemonDto> getByName(@PathVariable String name) {
        return pokemonService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}