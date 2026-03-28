package com.pokeapp.application.repository;

import com.pokeapp.domain.trainer.TeamSlotIv;
import com.pokeapp.domain.trainer.TeamSlotStatId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeamSlotIvRepository extends JpaRepository<TeamSlotIv, TeamSlotStatId> {
    List<TeamSlotIv> findByTeamSlotId(Integer teamSlotId);
}
