package com.pokeapp.domain.move;

import jakarta.persistence.*;

@Entity
@Table(name = "damage_class")
public class DamageClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 20)
    private String name;

    public DamageClass() {
    }

    public DamageClass(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}