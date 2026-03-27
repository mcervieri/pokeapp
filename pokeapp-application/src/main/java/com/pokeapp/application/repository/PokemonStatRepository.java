package com.pokeapp.application.repository;

import com.pokeapp.domain.pokemon.PokemonStat;
import com.pokeapp.domain.pokemon.PokemonStatId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PokemonStatRepository extends JpaRepository<PokemonStat, PokemonStatId> {
    List<PokemonStat> findByPokemonId(Integer pokemonId);
}