-- ============================================================
-- V1 — Core Pokémon tables
-- ============================================================

CREATE TABLE generation (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,  -- e.g. "generation-i"
    region      VARCHAR(50)                   -- e.g. "kanto"
);

CREATE TABLE type (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(20) NOT NULL UNIQUE   -- e.g. "fire", "water"
);

-- Type effectiveness matrix: how effective is attacking_type → defending_type?
CREATE TABLE type_effectiveness (
    attacking_type_id   INT NOT NULL REFERENCES type(id),
    defending_type_id   INT NOT NULL REFERENCES type(id),
    multiplier          NUMERIC(4,2) NOT NULL,  -- 0, 0.25, 0.5, 1, 2, 4
    PRIMARY KEY (attacking_type_id, defending_type_id)
);

CREATE TABLE pokemon_species (
    id              SERIAL PRIMARY KEY,
    pokeapi_id      INT NOT NULL UNIQUE,
    name            VARCHAR(100) NOT NULL UNIQUE,
    generation_id   INT NOT NULL REFERENCES generation(id),
    is_legendary    BOOLEAN NOT NULL DEFAULT FALSE,
    is_mythical     BOOLEAN NOT NULL DEFAULT FALSE,
    base_happiness  INT,
    capture_rate    INT
);

CREATE TABLE pokemon (
    id              SERIAL PRIMARY KEY,
    pokeapi_id      INT NOT NULL UNIQUE,
    name            VARCHAR(100) NOT NULL UNIQUE,   -- e.g. "garchomp-mega"
    species_id      INT NOT NULL REFERENCES pokemon_species(id),
    is_default      BOOLEAN NOT NULL DEFAULT TRUE,  -- base form vs. variant
    height_dm       INT,   -- decimetres (as PokéAPI returns)
    weight_hg       INT,   -- hectograms
    base_experience INT,
    sprite_front_url TEXT
);

-- A Pokémon can have 1 or 2 types
CREATE TABLE pokemon_type (
    pokemon_id  INT NOT NULL REFERENCES pokemon(id),
    type_id     INT NOT NULL REFERENCES type(id),
    slot        INT NOT NULL CHECK (slot IN (1, 2)),
    PRIMARY KEY (pokemon_id, slot)
);