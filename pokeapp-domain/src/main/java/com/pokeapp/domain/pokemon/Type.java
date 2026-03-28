package com.pokeapp.domain.pokemon;

import jakarta.persistence.*;

@Entity
@Table(name = "type")
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pokeapi_id", nullable = false, unique = true)
    private Integer pokeapiId;

    @Column(nullable = false, unique = true, length = 20)
    private String name;

    public Type() {
    }

    public Type(Integer pokeapiId, String name) {
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