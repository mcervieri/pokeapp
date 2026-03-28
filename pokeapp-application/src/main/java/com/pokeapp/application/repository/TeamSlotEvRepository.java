package com.pokeapp.application.repository;

import com.pokeapp.domain.trainer.TeamSlotEv;
import com.pokeapp.domain.trainer.TeamSlotStatId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeamSlotEvRepository extends JpaRepository<TeamSlotEv, TeamSlotStatId> {
    List<TeamSlotEv> findByTeamSlotId(Integer teamSlotId);
}