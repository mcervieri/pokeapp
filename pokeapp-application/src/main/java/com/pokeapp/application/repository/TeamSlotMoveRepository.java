package com.pokeapp.application.repository;

import com.pokeapp.domain.trainer.TeamSlotMove;
import com.pokeapp.domain.trainer.TeamSlotMoveId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeamSlotMoveRepository extends JpaRepository<TeamSlotMove, TeamSlotMoveId> {
    List<TeamSlotMove> findByTeamSlotIdOrderByIdSlot(Integer teamSlotId);
}