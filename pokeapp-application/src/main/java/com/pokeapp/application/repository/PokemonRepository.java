package com.pokeapp.application.repository;

import com.pokeapp.domain.pokemon.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PokemonRepository extends JpaRepository<Pokemon, Integer> {
    Optional<Pokemon> findByPokeapiId(Integer pokeapiId);

    Optional<Pokemon> findByName(String name);

    List<Pokemon> findBySpeciesId(Integer speciesId);
}