-- ============================================================
-- V3 — Moves
-- ============================================================

CREATE TABLE damage_class (
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(20) NOT NULL UNIQUE  -- physical, special, status
);

CREATE TABLE move (
    id              SERIAL PRIMARY KEY,
    pokeapi_id      INT NOT NULL UNIQUE,
    name            VARCHAR(100) NOT NULL UNIQUE,
    type_id         INT NOT NULL REFERENCES type(id),
    damage_class_id INT NOT NULL REFERENCES damage_class(id),
    power           INT,        -- NULL for status moves
    accuracy        INT,        -- NULL for moves that never miss
    pp              INT,
    priority        INT NOT NULL DEFAULT 0,
    effect_text     TEXT
);

-- Which moves can a Pokémon learn?
CREATE TABLE pokemon_move (
    pokemon_id      INT NOT NULL REFERENCES pokemon(id),
    move_id         INT NOT NULL REFERENCES move(id),
    learn_method    VARCHAR(50),   -- level-up, machine, egg, tutor
    level_learned   INT,
    PRIMARY KEY (pokemon_id, move_id, learn_method)
);