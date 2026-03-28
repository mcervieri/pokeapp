package com.pokeapp.domain.trainer;

import com.pokeapp.domain.item.Item;
import com.pokeapp.domain.pokemon.Ability;
import com.pokeapp.domain.pokemon.Nature;
import com.pokeapp.domain.pokemon.Pokemon;
import jakarta.persistence.*;

@Entity
@Table(name = "team_slot")
public class TeamSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(nullable = false)
    private Integer position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pokemon_id", nullable = false)
    private Pokemon pokemon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ability_id")
    private Ability ability;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nature_id")
    private Nature nature;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(length = 50)
    private String nickname;

    protected TeamSlot() {
    }

    public TeamSlot(Team team, Integer position, Pokemon pokemon) {
        this.team = team;
        this.position = position;
        this.pokemon = pokemon;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public void setNature(Nature nature) {
        this.nature = nature;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }

    public Integer getPosition() {
        return position;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public Ability getAbility() {
        return ability;
    }

    public Nature getNature() {
        return nature;
    }

    public Item getItem() {
        return item;
    }

    public String getNickname() {
        return nickname;
    }
}