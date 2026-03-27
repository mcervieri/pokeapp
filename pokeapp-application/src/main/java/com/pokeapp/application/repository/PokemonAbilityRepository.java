package com.pokeapp.application.repository;

import com.pokeapp.domain.pokemon.PokemonAbility;
import com.pokeapp.domain.pokemon.PokemonAbilityId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PokemonAbilityRepository extends JpaRepository<PokemonAbility, PokemonAbilityId> {
    List<PokemonAbility> findByPokemonId(Integer pokemonId);
}