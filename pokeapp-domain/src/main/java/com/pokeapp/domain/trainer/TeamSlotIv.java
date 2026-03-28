package com.pokeapp.domain.trainer;

import com.pokeapp.domain.pokemon.Stat;
import jakarta.persistence.*;

@Entity
@Table(name = "team_slot_iv")
public class TeamSlotIv {

    @EmbeddedId
    private TeamSlotStatId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("teamSlotId")
    @JoinColumn(name = "team_slot_id")
    private TeamSlot teamSlot;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("statId")
    @JoinColumn(name = "stat_id")
    private Stat stat;

    @Column(nullable = false)
    private Integer value; // 0–31

    protected TeamSlotIv() {
    }

    public TeamSlotIv(TeamSlot teamSlot, Stat stat, Integer value) {
        this.teamSlot = teamSlot;
        this.stat = stat;
        this.value = value;
        this.id = new TeamSlotStatId(teamSlot.getId(), stat.getId());
    }

    public TeamSlotStatId getId() {
        return id;
    }

    public TeamSlot getTeamSlot() {
        return teamSlot;
    }

    public Stat getStat() {
        return stat;
    }

    public Integer getValue() {
        return value;
    }
}