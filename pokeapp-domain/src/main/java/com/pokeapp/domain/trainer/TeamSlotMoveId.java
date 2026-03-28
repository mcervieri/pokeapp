package com.pokeapp.domain.trainer;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TeamSlotMoveId implements Serializable {

    private Integer teamSlotId;
    private Integer slot;

    protected TeamSlotMoveId() {
    }

    public TeamSlotMoveId(Integer teamSlotId, Integer slot) {
        this.teamSlotId = teamSlotId;
        this.slot = slot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TeamSlotMoveId that))
            return false;
        return Objects.equals(teamSlotId, that.teamSlotId)
                && Objects.equals(slot, that.slot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamSlotId, slot);
    }

    public Integer getTeamSlotId() {
        return teamSlotId;
    }

    public Integer getSlot() {
        return slot;
    }
}