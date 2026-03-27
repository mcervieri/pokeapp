package com.pokeapp.domain.pokemon;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PokemonAbilityId implements Serializable {

    private Integer pokemonId;
    private Integer slot;

    protected PokemonAbilityId() {
    }

    public PokemonAbilityId(Integer pokemonId, Integer slot) {
        this.pokemonId = pokemonId;
        this.slot = slot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PokemonAbilityId that))
            return false;
        return Objects.equals(pokemonId, that.pokemonId)
                && Objects.equals(slot, that.slot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pokemonId, slot);
    }

    public Integer getPokemonId() {
        return pokemonId;
    }

    public Integer getSlot() {
        return slot;
    }
}