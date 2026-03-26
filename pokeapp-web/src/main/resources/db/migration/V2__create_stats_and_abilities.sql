-- ============================================================
-- V2 — Base stats and abilities
-- ============================================================

CREATE TABLE stat (
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(30) NOT NULL UNIQUE  -- hp, attack, defense, ...
);

CREATE TABLE pokemon_stat (
    pokemon_id  INT NOT NULL REFERENCES pokemon(id),
    stat_id     INT NOT NULL REFERENCES stat(id),
    base_value  INT NOT NULL,
    effort      INT NOT NULL DEFAULT 0,  -- EV yield when defeated
    PRIMARY KEY (pokemon_id, stat_id)
);

CREATE TABLE ability (
    id          SERIAL PRIMARY KEY,
    pokeapi_id  INT NOT NULL UNIQUE,
    name        VARCHAR(100) NOT NULL UNIQUE,
    effect_text TEXT   -- English short effect
);

-- A Pokémon can have up to 3 abilities (slot 1, 2, hidden)
CREATE TABLE pokemon_ability (
    pokemon_id  INT NOT NULL REFERENCES pokemon(id),
    ability_id  INT NOT NULL REFERENCES ability(id),
    slot        INT NOT NULL CHECK (slot BETWEEN 1 AND 3),
    is_hidden   BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (pokemon_id, slot)
);