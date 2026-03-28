package com.pokeapp.domain.move;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PokemonMoveId implements Serializable {

    private Integer pokemonId;
    private Integer moveId;
    private String learnMethod;

    protected PokemonMoveId() {
    }

    public PokemonMoveId(Integer pokemonId, Integer moveId, String learnMethod) {
        this.pokemonId = pokemonId;
        this.moveId = moveId;
        this.learnMethod = learnMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PokemonMoveId that))
            return false;
        return Objects.equals(pokemonId, that.pokemonId)
                && Objects.equals(moveId, that.moveId)
                && Objects.equals(learnMethod, that.learnMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pokemonId, moveId, learnMethod);
    }

    public Integer getPokemonId() {
        return pokemonId;
    }

    public Integer getMoveId() {
        return moveId;
    }

    public String getLearnMethod() {
        return learnMethod;
    }
}