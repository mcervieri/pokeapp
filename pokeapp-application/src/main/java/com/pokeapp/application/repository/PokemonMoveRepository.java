package com.pokeapp.application.repository;

import com.pokeapp.domain.move.PokemonMove;
import com.pokeapp.domain.move.PokemonMoveId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PokemonMoveRepository extends JpaRepository<PokemonMove, PokemonMoveId> {
    List<PokemonMove> findByPokemonId(Integer pokemonId);

    List<PokemonMove> findByMoveId(Integer moveId);
}