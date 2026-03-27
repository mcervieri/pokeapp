package com.pokeapp.domain.pokemon;

import jakarta.persistence.*;

@Entity
@Table(name = "pokemon_stat")
public class PokemonStat {

    @EmbeddedId
    private PokemonStatId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pokemonId")
    @JoinColumn(name = "pokemon_id")
    private Pokemon pokemon;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("statId")
    @JoinColumn(name = "stat_id")
    private Stat stat;

    @Column(name = "base_value", nullable = false)
    private Integer baseValue;

    @Column(nullable = false)
    private Integer effort; // EV yield

    protected PokemonStat() {
    }

    public PokemonStat(Pokemon pokemon, Stat stat, Integer baseValue, Integer effort) {
        this.pokemon = pokemon;
        this.stat = stat;
        this.baseValue = baseValue;
        this.effort = effort;
        this.id = new PokemonStatId(pokemon.getId(), stat.getId());
    }

    public PokemonStatId getId() {
        return id;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public Stat getStat() {
        return stat;
    }

    public Integer getBaseValue() {
        return baseValue;
    }

    public Integer getEffort() {
        return effort;
    }
}