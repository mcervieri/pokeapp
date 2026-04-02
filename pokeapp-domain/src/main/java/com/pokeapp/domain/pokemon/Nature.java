package com.pokeapp.domain.pokemon;

import jakarta.persistence.*;

@Entity
@Table(name = "nature")
public class Nature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pokeapi_id", nullable = false, unique = true)
    private Integer pokeapiId;

    @Column(nullable = false, unique = true, length = 30)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "increased_stat_id")
    private Stat increasedStat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "decreased_stat_id")
    private Stat decreasedStat;

    public Nature() {
    }

    public Nature(Integer pokeapiId, String name, Stat increasedStat, Stat decreasedStat) {
        this.pokeapiId = pokeapiId;
        this.name = name;
        this.increasedStat = increasedStat;
        this.decreasedStat = decreasedStat;
    }

    public Integer getId() {
        return id;
    }

    public Integer getPokeapiId() {
        return pokeapiId;
    }

    public String getName() {
        return name;
    }

    public Stat getIncreasedStat() {
        return increasedStat;
    }

    public Stat getDecreasedStat() {
        return decreasedStat;
    }

    public boolean isNeutral() {
        return increasedStat == null && decreasedStat == null;
    }
}