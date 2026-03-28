package com.pokeapp.domain.item;

import jakarta.persistence.*;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pokeapi_id", nullable = false, unique = true)
    private Integer pokeapiId;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "effect_text", columnDefinition = "TEXT")
    private String effectText;

    @Column(name = "sprite_url")
    private String spriteUrl;

    protected Item() {
    }

    public Item(Integer pokeapiId, String name, String effectText, String spriteUrl) {
        this.pokeapiId = pokeapiId;
        this.name = name;
        this.effectText = effectText;
        this.spriteUrl = spriteUrl;
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

    public String getSpriteUrl() {
        return spriteUrl;
    }
}