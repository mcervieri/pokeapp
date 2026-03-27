package com.pokeapp.domain.pokemon;

import jakarta.persistence.*;

@Entity
@Table(name = "stat")
public class Stat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 30)
    private String name;

    protected Stat() {
    }

    public Stat(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}