package com.pokeapp.application.dto.team;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;

public record UpsertSlotRequest(

                @NotNull(message = "pokemonId is required") Integer pokemonId,

                Integer abilityId,
                Integer natureId,
                Integer itemId,

                @Size(max = 12, message = "Nickname must not exceed 12 characters") String nickname,

                @Size(max = 4, message = "A Pokémon can know at most 4 moves") List<Integer> moveIds,

                Map<Integer, Integer> evs,
                Map<Integer, Integer> ivs) {
}