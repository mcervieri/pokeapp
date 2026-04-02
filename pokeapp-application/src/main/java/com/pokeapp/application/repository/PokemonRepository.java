package com.pokeapp.application.repository;

import com.pokeapp.domain.pokemon.Pokemon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PokemonRepository extends JpaRepository<Pokemon, Integer> {

        Optional<Pokemon> findByPokeapiId(Integer pokeapiId);

        Optional<Pokemon> findByName(String name);

        List<Pokemon> findBySpeciesId(Integer speciesId);

        @Query("""
                        SELECT DISTINCT p FROM Pokemon p
                        LEFT JOIN p.pokemonTypes pt
                        LEFT JOIN pt.type t
                        WHERE (CAST(:typeName AS string) IS NULL OR LOWER(t.name) = LOWER(CAST(:typeName AS string)))
                        AND (CAST(:search AS string) IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')))
                        """)
        Page<Pokemon> findByFilters(
                        @Param("typeName") String typeName,
                        @Param("search") String search,
                        Pageable pageable);
}