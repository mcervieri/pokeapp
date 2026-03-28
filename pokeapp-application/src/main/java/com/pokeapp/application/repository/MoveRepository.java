package com.pokeapp.application.repository;

import com.pokeapp.domain.move.Move;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MoveRepository extends JpaRepository<Move, Integer> {
    Optional<Move> findByPokeapiId(Integer pokeapiId);

    Optional<Move> findByName(String name);
}