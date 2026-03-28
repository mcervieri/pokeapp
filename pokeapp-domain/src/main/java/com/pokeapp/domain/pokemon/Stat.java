package com.pokeapp.domain.pokemon;

import jakarta.persistence.*;

@Entity
@Table(name = "stat")
public class Stat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pokeapi_id", nullable = false, unique = true)
    private Integer pokeapiId;

    @Column(nullable = false, unique = true, length = 30)
    private String name;

    public Stat() {
    }

    public Stat(Integer pokeapiId, String name) {
        this.pokeapiId = pokeapiId;
        this.name = name;
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
}