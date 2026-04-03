package com.pokeapp.web.controller;

import com.pokeapp.application.dto.user.RatingDto;
import com.pokeapp.application.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PutMapping("/pokemon/{pokemonId}/rating")
    public ResponseEntity<RatingDto> rate(@PathVariable Integer pokemonId,
            @RequestParam Integer score,
            Authentication authentication) {
        return ResponseEntity.ok(
                ratingService.rateOrUpdate(authentication.getName(), pokemonId, score));
    }

    @GetMapping("/pokemon/{pokemonId}/rating/average")
    public ResponseEntity<Double> getAverage(@PathVariable Integer pokemonId) {
        Double avg = ratingService.getAverageRating(pokemonId);
        return ResponseEntity.ok(avg != null ? avg : 0.0);
    }

    @DeleteMapping("/pokemon/{pokemonId}/rating")
    public ResponseEntity<Void> deleteRating(@PathVariable Integer pokemonId,
            Authentication authentication) {
        ratingService.deleteRating(authentication.getName(), pokemonId);
        return ResponseEntity.noContent().build();
    }
}