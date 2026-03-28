package com.pokeapp.domain.trainer;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TeamSlotStatId implements Serializable {

    private Integer teamSlotId;
    private Integer statId;

    protected TeamSlotStatId() {
    }

    public TeamSlotStatId(Integer teamSlotId, Integer statId) {
        this.teamSlotId = teamSlotId;
        this.statId = statId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TeamSlotStatId that))
            return false;
        return Objects.equals(teamSlotId, that.teamSlotId)
                && Objects.equals(statId, that.statId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamSlotId, statId);
    }

    public Integer getTeamSlotId() {
        return teamSlotId;
    }

    public Integer getStatId() {
        return statId;
    }
}