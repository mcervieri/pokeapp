-- ============================================================
-- V100 – User feature tables: favorites, comments, ratings, pokédex
-- ============================================================

CREATE TABLE trainer_favorite (
    trainer_id  INT NOT NULL REFERENCES trainer(id) ON DELETE CASCADE,
    pokemon_id  INT NOT NULL REFERENCES pokemon(id) ON DELETE CASCADE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (trainer_id, pokemon_id)
);

CREATE TABLE trainer_comment (
    id          SERIAL PRIMARY KEY,
    trainer_id  INT NOT NULL REFERENCES trainer(id) ON DELETE CASCADE,
    pokemon_id  INT NOT NULL REFERENCES pokemon(id) ON DELETE CASCADE,
    body        TEXT NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE trainer_rating (
    trainer_id  INT NOT NULL REFERENCES trainer(id) ON DELETE CASCADE,
    pokemon_id  INT NOT NULL REFERENCES pokemon(id) ON DELETE CASCADE,
    score       INT NOT NULL CHECK (score BETWEEN 1 AND 5),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (trainer_id, pokemon_id)
);

CREATE TABLE trainer_pokedex (
    trainer_id  INT NOT NULL REFERENCES trainer(id) ON DELETE CASCADE,
    pokemon_id  INT NOT NULL REFERENCES pokemon(id) ON DELETE CASCADE,
    seen        BOOLEAN NOT NULL DEFAULT FALSE,
    caught      BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (trainer_id, pokemon_id)
);