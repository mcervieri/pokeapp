package com.pokeapp.domain.trainer;

import com.pokeapp.domain.pokemon.Pokemon;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "trainer_favorite")
public class TrainerFavorite {

    @EmbeddedId
    private TrainerFavoriteId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("trainerId")
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pokemonId")
    @JoinColumn(name = "pokemon_id")
    private Pokemon pokemon;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected TrainerFavorite() {
    }

    public TrainerFavorite(Trainer trainer, Pokemon pokemon) {
        this.id = new TrainerFavoriteId(trainer.getId(), pokemon.getId());
        this.trainer = trainer;
        this.pokemon = pokemon;
        this.createdAt = OffsetDateTime.now();
    }

    public TrainerFavoriteId getId() {
        return id;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}