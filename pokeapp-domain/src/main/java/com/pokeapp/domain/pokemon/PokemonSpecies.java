package com.pokeapp.domain.pokemon;

import jakarta.persistence.*;

@Entity
@Table(name = "pokemon_species")
public class PokemonSpecies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pokeapi_id", nullable = false, unique = true)
    private Integer pokeapiId;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generation_id", nullable = false)
    private Generation generation;

    @Column(name = "is_legendary", nullable = false)
    private boolean legendary;

    @Column(name = "is_mythical", nullable = false)
    private boolean mythical;

    @Column(name = "base_happiness")
    private Integer baseHappiness;

    @Column(name = "capture_rate")
    private Integer captureRate;

    public PokemonSpecies() {
    }

    public PokemonSpecies(Integer pokeapiId, String name, Generation generation,
            boolean legendary, boolean mythical,
            Integer baseHappiness, Integer captureRate) {
        this.pokeapiId = pokeapiId;
        this.name = name;
        this.generation = generation;
        this.legendary = legendary;
        this.mythical = mythical;
        this.baseHappiness = baseHappiness;
        this.captureRate = captureRate;
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

    public Generation getGeneration() {
        return generation;
    }

    public boolean isLegendary() {
        return legendary;
    }

    public boolean isMythical() {
        return mythical;
    }

    public Integer getBaseHappiness() {
        return baseHappiness;
    }

    public Integer getCaptureRate() {
        return captureRate;
    }
}