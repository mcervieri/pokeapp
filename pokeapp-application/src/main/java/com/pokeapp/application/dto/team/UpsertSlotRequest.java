package com.pokeapp.application.dto.team;

import java.util.List;
import java.util.Map;

public record UpsertSlotRequest(
        Integer pokemonId,
        Integer abilityId,
        Integer natureId,
        Integer itemId,
        String nickname,
        List<Integer> moveIds, // up to 4, in order (slot 1–4)
        Map<Integer, Integer> evs, // statId → value (0–252, total ≤ 510)
        Map<Integer, Integer> ivs // statId → value (0–31)
) {
}