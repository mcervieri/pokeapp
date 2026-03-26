-- ============================================================
-- V4 — Items and Natures
-- ============================================================

CREATE TABLE item (
    id          SERIAL PRIMARY KEY,
    pokeapi_id  INT NOT NULL UNIQUE,
    name        VARCHAR(100) NOT NULL UNIQUE,
    effect_text TEXT,
    sprite_url  TEXT
);

CREATE TABLE nature (
    id              SERIAL PRIMARY KEY,
    pokeapi_id      INT NOT NULL UNIQUE,
    name            VARCHAR(30) NOT NULL UNIQUE,  -- e.g. "jolly"
    increased_stat_id INT REFERENCES stat(id),    -- NULL for neutral natures
    decreased_stat_id INT REFERENCES stat(id)
);