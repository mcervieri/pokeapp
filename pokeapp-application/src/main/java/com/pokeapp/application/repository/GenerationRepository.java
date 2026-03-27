package com.pokeapp.application.repository;

import com.pokeapp.domain.pokemon.Generation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GenerationRepository extends JpaRepository<Generation, Integer> {
    Optional<Generation> findByName(String name);
}