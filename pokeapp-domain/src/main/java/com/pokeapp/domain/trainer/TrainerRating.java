package com.pokeapp.domain.trainer;

import com.pokeapp.domain.pokemon.Pokemon;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "trainer_rating")
public class TrainerRating {

    @EmbeddedId
    private TrainerRatingId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("trainerId")
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pokemonId")
    @JoinColumn(name = "pokemon_id")
    private Pokemon pokemon;

    @Column(nullable = false)
    private Integer score;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected TrainerRating() {
    }

    public TrainerRating(Trainer trainer, Pokemon pokemon, Integer score) {
        this.id = new TrainerRatingId(trainer.getId(), pokemon.getId());
        this.trainer = trainer;
        this.pokemon = pokemon;
        this.score = score;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public TrainerRatingId getId() {
        return id;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public Integer getScore() {
        return score;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void updateScore(Integer newScore) {
        this.score = newScore;
        this.updatedAt = OffsetDateTime.now();
    }
}