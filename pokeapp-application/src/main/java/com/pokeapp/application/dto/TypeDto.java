package com.pokeapp.application.dto;

import java.util.List;

public record TypeDto(
                Integer id,
                String name,
                List<TypeMatchupDto> damageRelations) {
}