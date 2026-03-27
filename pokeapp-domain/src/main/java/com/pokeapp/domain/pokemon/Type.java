package com.pokeapp.domain.pokemon;

import jakarta.persistence.*;

@Entity
@Table(name = "type")
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 20)
    private String name;

    protected Type() {
    }

    public Type(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}