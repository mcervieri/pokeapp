package com.pokeapp.application.dto.team;

import java.time.OffsetDateTime;

public record TeamSummaryDto(
        Integer id,
        String name,
        String format,
        int slotCount,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {
}