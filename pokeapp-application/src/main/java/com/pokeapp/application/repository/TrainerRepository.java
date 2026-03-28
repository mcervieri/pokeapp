package com.pokeapp.application.repository;

import com.pokeapp.domain.trainer.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Integer> {
    Optional<Trainer> findByUsername(String username);
}