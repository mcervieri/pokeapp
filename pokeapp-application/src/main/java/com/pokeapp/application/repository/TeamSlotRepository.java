package com.pokeapp.application.repository;

import com.pokeapp.domain.trainer.TeamSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TeamSlotRepository extends JpaRepository<TeamSlot, Integer> {
    List<TeamSlot> findByTeamIdOrderByPosition(Integer teamId);

    Optional<TeamSlot> findByTeamIdAndPosition(Integer teamId, Integer position);
}