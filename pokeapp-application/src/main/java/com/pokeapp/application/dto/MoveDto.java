package com.pokeapp.application.dto;

public record MoveDto(
                Integer id,
                String name,
                String type,
                String damageClass,
                Integer power,
                Integer accuracy,
                Integer pp) {
}