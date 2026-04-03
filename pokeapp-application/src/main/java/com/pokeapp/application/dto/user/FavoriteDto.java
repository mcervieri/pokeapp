package com.pokeapp.application.dto.user;

import java.time.OffsetDateTime;

public record FavoriteDto(
                Integer pokemonId,
                String pokemonName,
                String spriteUrl,
                OffsetDateTime createdAt) {
}