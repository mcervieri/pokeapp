package com.pokeapp.application.dto.user;

import java.time.OffsetDateTime;

public record CommentDto(
        Integer id,
        Integer pokemonId,
        String username,
        String body,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {
}