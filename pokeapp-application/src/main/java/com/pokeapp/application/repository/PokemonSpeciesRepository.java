package com.pokeapp.application.repository;

import com.pokeapp.domain.pokemon.PokemonSpecies;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PokemonSpeciesRepository extends JpaRepository<PokemonSpecies, Integer> {
    Optional<PokemonSpecies> findByPokeapiId(Integer pokeapiId);

    Optional<PokemonSpecies> findByName(String name);
}