package com.pokeapp.domain.pokemon;

import jakarta.persistence.*;

@Entity
@Table(name = "evolution_chain")
public class EvolutionChain {

    @Id
    @Column(name = "id")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}