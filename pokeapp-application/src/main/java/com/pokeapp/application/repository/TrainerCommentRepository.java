package com.pokeapp.application.repository;

import com.pokeapp.domain.trainer.TrainerComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainerCommentRepository extends JpaRepository<TrainerComment, Integer> {

    List<TrainerComment> findByPokemonIdOrderByCreatedAtDesc(Integer pokemonId);

    Optional<TrainerComment> findByIdAndTrainerId(Integer id, Integer trainerId);
}