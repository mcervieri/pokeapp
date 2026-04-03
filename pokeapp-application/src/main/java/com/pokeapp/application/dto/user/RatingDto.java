package com.pokeapp.application.dto.user;

import java.time.OffsetDateTime;

public record RatingDto(
        Integer pokemonId,
        String username,
        Integer score,
        OffsetDateTime updatedAt) {
}