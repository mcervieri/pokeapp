package com.pokeapp.application.repository;

import com.pokeapp.domain.pokemon.Ability;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AbilityRepository extends JpaRepository<Ability, Integer> {

    Optional<Ability> findByPokeapiId(Integer pokeapiId);

    Optional<Ability> findByName(String name);

    @Query("SELECT a FROM Ability a WHERE :search IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Ability> findBySearch(@Param("search") String search, Pageable pageable);
}