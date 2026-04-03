package com.pokeapp.application.repository;

import com.pokeapp.domain.trainer.TrainerRating;
import com.pokeapp.domain.trainer.TrainerRatingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TrainerRatingRepository
        extends JpaRepository<TrainerRating, TrainerRatingId> {

    Optional<TrainerRating> findByIdTrainerIdAndIdPokemonId(
            Integer trainerId, Integer pokemonId);

    @Query("SELECT AVG(r.score) FROM TrainerRating r WHERE r.id.pokemonId = :pokemonId")
    Double findAverageScoreByPokemonId(@Param("pokemonId") Integer pokemonId);
}