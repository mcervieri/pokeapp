package com.pokeapp.domain.pokemon;

import jakarta.persistence.*;

@Entity
@Table(name = "pokemon")
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pokeapi_id", nullable = false, unique = true)
    private Integer pokeapiId;

    @Column(nullable = false, unique = true, length = 100)
    private String name; // "garchomp", "garchomp-mega"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id", nullable = false)
    private PokemonSpecies species;

    @Column(name = "is_default", nullable = false)
    private boolean defaultForm;

    @Column(name = "height_dm")
    private Integer heightDm;

    @Column(name = "weight_hg")
    private Integer weightHg;

    @Column(name = "base_experience")
    private Integer baseExperience;

    @Column(name = "sprite_front_url")
    private String spriteFrontUrl;

    protected Pokemon() {
    }

    public Pokemon(Integer pokeapiId, String name, PokemonSpecies species,
            boolean defaultForm, Integer heightDm, Integer weightHg,
            Integer baseExperience, String spriteFrontUrl) {
        this.pokeapiId = pokeapiId;
        this.name = name;
        this.species = species;
        this.defaultForm = defaultForm;
        this.heightDm = heightDm;
        this.weightHg = weightHg;
        this.baseExperience = baseExperience;
        this.spriteFrontUrl = spriteFrontUrl;
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

    public PokemonSpecies getSpecies() {
        return species;
    }

    public boolean isDefaultForm() {
        return defaultForm;
    }

    public Integer getHeightDm() {
        return heightDm;
    }

    public Integer getWeightHg() {
        return weightHg;
    }

    public Integer getBaseExperience() {
        return baseExperience;
    }

    public String getSpriteFrontUrl() {
        return spriteFrontUrl;
    }
}