package com.pokeapp.application.repository;

import com.pokeapp.domain.pokemon.PokemonType;
import com.pokeapp.domain.pokemon.PokemonTypeId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PokemonTypeRepository extends JpaRepository<PokemonType, PokemonTypeId> {
    List<PokemonType> findByPokemonId(Integer pokemonId);
}