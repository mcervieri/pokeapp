package com.pokeapp.application.repository;

import com.pokeapp.domain.trainer.TrainerPokedex;
import com.pokeapp.domain.trainer.TrainerPokedexId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainerPokedexRepository
        extends JpaRepository<TrainerPokedex, TrainerPokedexId> {

    Optional<TrainerPokedex> findByIdTrainerIdAndIdPokemonId(
            Integer trainerId, Integer pokemonId);

    List<TrainerPokedex> findByTrainerIdAndCaughtTrue(Integer trainerId);

    List<TrainerPokedex> findByTrainerIdAndSeenTrue(Integer trainerId);
}