package com.pokeapp.domain.move;

import com.pokeapp.domain.pokemon.Pokemon;
import jakarta.persistence.*;

@Entity
@Table(name = "pokemon_move")
public class PokemonMove {

    @EmbeddedId
    private PokemonMoveId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pokemonId")
    @JoinColumn(name = "pokemon_id")
    private Pokemon pokemon;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("moveId")
    @JoinColumn(name = "move_id")
    private Move move;

    @Column(name = "level_learned")
    private Integer levelLearned;

    protected PokemonMove() {
    }

    public PokemonMove(Pokemon pokemon, Move move, String learnMethod, Integer levelLearned) {
        this.pokemon = pokemon;
        this.move = move;
        this.levelLearned = levelLearned;
        this.id = new PokemonMoveId(pokemon.getId(), move.getId(), learnMethod);
    }

    public PokemonMoveId getId() {
        return id;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public Move getMove() {
        return move;
    }

    public String getLearnMethod() {
        return id.getLearnMethod();
    }

    public Integer getLevelLearned() {
        return levelLearned;
    }
}