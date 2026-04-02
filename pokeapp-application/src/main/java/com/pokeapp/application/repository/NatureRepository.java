package com.pokeapp.application.repository;

import com.pokeapp.domain.pokemon.Nature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NatureRepository extends JpaRepository<Nature, Integer> {

    Optional<Nature> findByPokeapiId(Integer pokeapiId);

    Optional<Nature> findByName(String name);

    List<Nature> findByIncreasedStatId(Integer statId);

    List<Nature> findByDecreasedStatId(Integer statId);

    @Query("SELECT n FROM Nature n WHERE :search IS NULL OR LOWER(n.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Nature> findBySearch(@Param("search") String search, Pageable pageable);
}