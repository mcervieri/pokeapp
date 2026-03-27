package com.pokeapp.domain.pokemon;

import jakarta.persistence.*;

@Entity
@Table(name = "pokemon_type")
public class PokemonType {

    @EmbeddedId
    private PokemonTypeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pokemonId")
    @JoinColumn(name = "pokemon_id")
    private Pokemon pokemon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;

    protected PokemonType() {
    }

    public PokemonType(Pokemon pokemon, Type type, Integer slot) {
        this.pokemon = pokemon;
        this.type = type;
        this.id = new PokemonTypeId(pokemon.getId(), slot);
    }

    public PokemonTypeId getId() {
        return id;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public Type getType() {
        return type;
    }

    public Integer getSlot() {
        return id.getSlot();
    }
}