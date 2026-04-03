package com.pokeapp.application.dto.user;

import java.time.OffsetDateTime;

public record PokedexEntryDto(
        Integer pokemonId,
        String pokemonName,
        String spriteUrl,
        Boolean seen,
        Boolean caught,
        OffsetDateTime updatedAt) {
}