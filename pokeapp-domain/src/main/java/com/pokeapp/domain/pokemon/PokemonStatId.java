package com.pokeapp.domain.pokemon;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PokemonStatId implements Serializable {

    private Integer pokemonId;
    private Integer statId;

    protected PokemonStatId() {
    }

    public PokemonStatId(Integer pokemonId, Integer statId) {
        this.pokemonId = pokemonId;
        this.statId = statId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PokemonStatId that))
            return false;
        return Objects.equals(pokemonId, that.pokemonId)
                && Objects.equals(statId, that.statId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pokemonId, statId);
    }

    public Integer getPokemonId() {
        return pokemonId;
    }

    public Integer getStatId() {
        return statId;
    }
}