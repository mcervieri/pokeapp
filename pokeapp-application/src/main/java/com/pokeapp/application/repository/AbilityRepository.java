package com.pokeapp.application.repository;

import com.pokeapp.domain.pokemon.Ability;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AbilityRepository extends JpaRepository<Ability, Integer> {
    Optional<Ability> findByPokeapiId(Integer pokeapiId);

    Optional<Ability> findByName(String name);
}