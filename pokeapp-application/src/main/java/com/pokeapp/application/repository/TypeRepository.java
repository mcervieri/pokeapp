package com.pokeapp.application.repository;

import com.pokeapp.domain.move.Move;
import com.pokeapp.domain.pokemon.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TypeRepository extends JpaRepository<Type, Integer> {
    Optional<Type> findByName(String name);

    @Query("SELECT m FROM Move m WHERE :search IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Move> findBySearch(@Param("search") String search, Pageable pageable);
}
