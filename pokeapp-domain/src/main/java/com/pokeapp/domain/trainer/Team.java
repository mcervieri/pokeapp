package com.pokeapp.domain.trainer;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 30)
    private String format;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected Team() {
    }

    public Team(Trainer trainer, String name, String format) {
        this.trainer = trainer;
        this.name = name;
        this.format = format;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public void touch() {
        this.updatedAt = OffsetDateTime.now();
    }

    public void update(String name, String format) {
        this.name = name;
        this.format = format;
        this.updatedAt = OffsetDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

}