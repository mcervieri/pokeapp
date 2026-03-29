package com.pokeapp.application.dto;

import java.math.BigDecimal;

public record TypeMatchupDto(
                String targetType,
                BigDecimal multiplier) {
}