package com.pokeapp.application.repository;

import com.pokeapp.domain.move.Move;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MoveRepository extends JpaRepository<Move, Integer> {
    Optional<Move> findByPokeapiId(Integer pokeapiId);

    Optional<Move> findByName(String name);

    @Query("SELECT m FROM Move m WHERE :search IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Move> findBySearch(@Param("search") String search, Pageable pageable);
}