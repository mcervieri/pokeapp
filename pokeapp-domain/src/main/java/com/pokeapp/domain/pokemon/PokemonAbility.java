package com.pokeapp.domain.pokemon;

import jakarta.persistence.*;

@Entity
@Table(name = "pokemon_ability")
public class PokemonAbility {

    @EmbeddedId
    private PokemonAbilityId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pokemonId")
    @JoinColumn(name = "pokemon_id")
    private Pokemon pokemon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ability_id", nullable = false)
    private Ability ability;

    @Column(name = "is_hidden", nullable = false)
    private boolean hidden;

    protected PokemonAbility() {
    }

    public PokemonAbility(Pokemon pokemon, Ability ability, Integer slot, boolean hidden) {
        this.pokemon = pokemon;
        this.ability = ability;
        this.hidden = hidden;
        this.id = new PokemonAbilityId(pokemon.getId(), slot);
    }

    public PokemonAbilityId getId() {
        return id;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public Ability getAbility() {
        return ability;
    }

    public Integer getSlot() {
        return id.getSlot();
    }

    public boolean isHidden() {
        return hidden;
    }
}