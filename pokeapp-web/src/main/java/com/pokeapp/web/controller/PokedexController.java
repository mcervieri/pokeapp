package com.pokeapp.web.controller;

import com.pokeapp.application.dto.user.PokedexEntryDto;
import com.pokeapp.application.service.PokedexService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pokedex")
public class PokedexController {

    private final PokedexService pokedexService;

    public PokedexController(PokedexService pokedexService) {
        this.pokedexService = pokedexService;
    }

    @PostMapping("/{pokemonId}/seen")
    public ResponseEntity<PokedexEntryDto> markSeen(@PathVariable Integer pokemonId,
            Authentication authentication) {
        return ResponseEntity.ok(
                pokedexService.markSeen(authentication.getName(), pokemonId));
    }

    @PostMapping("/{pokemonId}/caught")
    public ResponseEntity<PokedexEntryDto> markCaught(@PathVariable Integer pokemonId,
            Authentication authentication) {
        return ResponseEntity.ok(
                pokedexService.markCaught(authentication.getName(), pokemonId));
    }

    @PostMapping("/{pokemonId}/uncaught")
    public ResponseEntity<PokedexEntryDto> unmarkCaught(@PathVariable Integer pokemonId,
            Authentication authentication) {
        return ResponseEntity.ok(
                pokedexService.unmarkCaught(authentication.getName(), pokemonId));
    }

    @GetMapping("/caught")
    public ResponseEntity<List<PokedexEntryDto>> getCaught(Authentication authentication) {
        return ResponseEntity.ok(
                pokedexService.getCaught(authentication.getName()));
    }

    @GetMapping("/seen")
    public ResponseEntity<List<PokedexEntryDto>> getSeen(Authentication authentication) {
        return ResponseEntity.ok(
                pokedexService.getSeen(authentication.getName()));
    }
}