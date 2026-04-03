package com.pokeapp.web.controller;

import com.pokeapp.application.dto.user.FavoriteDto;
import com.pokeapp.application.service.FavoriteService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorites")
@Tag(name = "Favorites", description = "Trainer Pokémon favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/{pokemonId}")
    public ResponseEntity<Void> addFavorite(@PathVariable Integer pokemonId,
            Authentication authentication) {
        favoriteService.addFavorite(authentication.getName(), pokemonId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{pokemonId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Integer pokemonId,
            Authentication authentication) {
        favoriteService.removeFavorite(authentication.getName(), pokemonId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteDto>> getFavorites(Authentication authentication) {
        return ResponseEntity.ok(favoriteService.getFavorites(authentication.getName()));
    }
}