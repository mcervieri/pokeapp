package com.pokeapp.domain.trainer;

import com.pokeapp.domain.pokemon.Pokemon;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "trainer_pokedex")
public class TrainerPokedex {

    @EmbeddedId
    private TrainerPokedexId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("trainerId")
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pokemonId")
    @JoinColumn(name = "pokemon_id")
    private Pokemon pokemon;

    @Column(nullable = false)
    private Boolean seen;

    @Column(nullable = false)
    private Boolean caught;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected TrainerPokedex() {
    }

    public TrainerPokedex(Trainer trainer, Pokemon pokemon) {
        this.id = new TrainerPokedexId(trainer.getId(), pokemon.getId());
        this.trainer = trainer;
        this.pokemon = pokemon;
        this.seen = false;
        this.caught = false;
        this.updatedAt = OffsetDateTime.now();
    }

    public TrainerPokedexId getId() {
        return id;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public Boolean getSeen() {
        return seen;
    }

    public Boolean getCaught() {
        return caught;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void markSeen() {
        this.seen = true;
        this.updatedAt = OffsetDateTime.now();
    }

    public void markCaught() {
        this.seen = true; // caught implies seen
        this.caught = true;
        this.updatedAt = OffsetDateTime.now();
    }

    public void unmarkCaught() {
        this.caught = false;
        this.updatedAt = OffsetDateTime.now();
    }
}