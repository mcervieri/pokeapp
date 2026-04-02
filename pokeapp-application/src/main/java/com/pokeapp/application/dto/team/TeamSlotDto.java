package com.pokeapp.application.dto.team;

import java.util.List;
import java.util.Map;

public record TeamSlotDto(
        Integer id,
        Integer position,
        Integer pokemonId,
        String pokemonName,
        String pokemonSpriteUrl,
        String abilityName,
        String natureName,
        String itemName,
        String nickname,
        List<String> moves, // move names in slot order
        Map<String, Integer> evs, // stat name → value
        Map<String, Integer> ivs // stat name → value
) {
}