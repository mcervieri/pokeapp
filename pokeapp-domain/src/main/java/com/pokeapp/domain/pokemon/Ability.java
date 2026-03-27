package com.pokeapp.domain.pokemon;

import jakarta.persistence.*;

@Entity
@Table(name = "ability")
public class Ability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pokeapi_id", nullable = false, unique = true)
    private Integer pokeapiId;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "effect_text", columnDefinition = "TEXT")
    private String effectText;

    protected Ability() {
    }

    public Ability(Integer pokeapiId, String name, String effectText) {
        this.pokeapiId = pokeapiId;
        this.name = name;
        this.effectText = effectText;
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

    public String getEffectText() {
        return effectText;
    }
}