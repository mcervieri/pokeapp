package com.pokeapp.domain.pokemon;

import jakarta.persistence.*;

@Entity
@Table(name = "generation")
public class Generation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 50)
    private String region;

    protected Generation() {
    }

    public Generation(String name, String region) {
        this.name = name;
        this.region = region;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }
}