package com.pokeapp.application.dto.team;

import java.time.OffsetDateTime;
import java.util.List;

public record TeamDetailDto(
        Integer id,
        String name,
        String format,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        List<TeamSlotDto> slots) {
}