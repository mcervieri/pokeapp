package com.pokeapp.application.dto;

import java.util.List;

public record PokemonDto(
                Integer id,
                String name,
                Integer height,
                Integer weight,
                Integer baseExperience,
                List<String> types,
                List<StatValueDto> stats) {
}