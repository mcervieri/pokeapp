package com.pokeapp.domain.pokemon;

import jakarta.persistence.*;

@Entity
@Table(name = "generation")
public class Generation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pokeapi_id", nullable = false, unique = true)
    private Integer pokeapiId;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 50)
    private String region;

    public Generation() {
    }

    public Generation(Integer pokeapiId, String name, String region) {
        this.pokeapiId = pokeapiId;
        this.name = name;
        this.region = region;
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

    public String getRegion() {
        return region;
    }
}