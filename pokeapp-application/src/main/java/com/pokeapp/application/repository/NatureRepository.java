package com.pokeapp.application.repository;

import com.pokeapp.domain.pokemon.Nature;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NatureRepository extends JpaRepository<Nature, Integer> {
    Optional<Nature> findByPokeapiId(Integer pokeapiId);

    Optional<Nature> findByName(String name);

    List<Nature> findByIncreasedStatId(Integer statId);

    List<Nature> findByDecreasedStatId(Integer statId);
}