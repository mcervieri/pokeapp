package com.pokeapp.application.repository;

import com.pokeapp.domain.trainer.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    List<Team> findByTrainerId(Integer trainerId);
}