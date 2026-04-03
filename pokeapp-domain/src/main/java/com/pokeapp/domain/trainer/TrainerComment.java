package com.pokeapp.domain.trainer;

import com.pokeapp.domain.pokemon.Pokemon;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "trainer_comment")
public class TrainerComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pokemon_id", nullable = false)
    private Pokemon pokemon;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected TrainerComment() {
    }

    public TrainerComment(Trainer trainer, Pokemon pokemon, String body) {
        this.trainer = trainer;
        this.pokemon = pokemon;
        this.body = body;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public String getBody() {
        return body;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void updateBody(String newBody) {
        this.body = newBody;
        this.updatedAt = OffsetDateTime.now();
    }
}