package com.pokeapp.domain.trainer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TrainerPokedexId implements Serializable {

    @Column(name = "trainer_id")
    private Integer trainerId;

    @Column(name = "pokemon_id")
    private Integer pokemonId;

    protected TrainerPokedexId() {
    }

    public TrainerPokedexId(Integer trainerId, Integer pokemonId) {
        this.trainerId = trainerId;
        this.pokemonId = pokemonId;
    }

    public Integer getTrainerId() {
        return trainerId;
    }

    public Integer getPokemonId() {
        return pokemonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TrainerPokedexId that))
            return false;
        return Objects.equals(trainerId, that.trainerId) &&
                Objects.equals(pokemonId, that.pokemonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainerId, pokemonId);
    }
}