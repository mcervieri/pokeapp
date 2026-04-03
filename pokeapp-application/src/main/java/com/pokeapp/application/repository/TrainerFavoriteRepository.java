package com.pokeapp.application.repository;

import com.pokeapp.domain.trainer.TrainerFavorite;
import com.pokeapp.domain.trainer.TrainerFavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrainerFavoriteRepository
        extends JpaRepository<TrainerFavorite, TrainerFavoriteId> {

    boolean existsById(TrainerFavoriteId id);

    @Query("SELECT f FROM TrainerFavorite f JOIN FETCH f.pokemon WHERE f.trainer.id = :trainerId")
    List<TrainerFavorite> findByTrainerId(@Param("trainerId") Integer trainerId);
}