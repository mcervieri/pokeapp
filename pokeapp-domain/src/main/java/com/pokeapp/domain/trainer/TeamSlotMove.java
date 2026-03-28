package com.pokeapp.domain.trainer;

import com.pokeapp.domain.move.Move;
import jakarta.persistence.*;

@Entity
@Table(name = "team_slot_move")
public class TeamSlotMove {

    @EmbeddedId
    private TeamSlotMoveId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("teamSlotId")
    @JoinColumn(name = "team_slot_id")
    private TeamSlot teamSlot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "move_id", nullable = false)
    private Move move;

    protected TeamSlotMove() {
    }

    public TeamSlotMove(TeamSlot teamSlot, Move move, Integer slot) {
        this.teamSlot = teamSlot;
        this.move = move;
        this.id = new TeamSlotMoveId(teamSlot.getId(), slot);
    }

    public TeamSlotMoveId getId() {
        return id;
    }

    public TeamSlot getTeamSlot() {
        return teamSlot;
    }

    public Move getMove() {
        return move;
    }

    public Integer getSlot() {
        return id.getSlot();
    }
}