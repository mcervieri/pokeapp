package com.pokeapp.application.repository;

import com.pokeapp.domain.pokemon.Stat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StatRepository extends JpaRepository<Stat, Integer> {
    Optional<Stat> findByName(String name);
}