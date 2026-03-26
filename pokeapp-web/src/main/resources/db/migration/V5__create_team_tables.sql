-- ============================================================
-- V5 — Teams and team slots
-- ============================================================

CREATE TABLE trainer (
    id          SERIAL PRIMARY KEY,
    username    VARCHAR(50) NOT NULL UNIQUE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE team (
    id          SERIAL PRIMARY KEY,
    trainer_id  INT NOT NULL REFERENCES trainer(id),
    name        VARCHAR(100) NOT NULL,
    format      VARCHAR(30),       -- "OU", "VGC", "Ubers", etc.
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Each team has up to 6 slots (position 1–6)
CREATE TABLE team_slot (
    id          SERIAL PRIMARY KEY,
    team_id     INT NOT NULL REFERENCES team(id) ON DELETE CASCADE,
    position    INT NOT NULL CHECK (position BETWEEN 1 AND 6),
    pokemon_id  INT NOT NULL REFERENCES pokemon(id),
    ability_id  INT REFERENCES ability(id),
    nature_id   INT REFERENCES nature(id),
    item_id     INT REFERENCES item(id),
    nickname    VARCHAR(50),
    UNIQUE (team_id, position)
);

-- Each slot can have up to 4 moves
CREATE TABLE team_slot_move (
    team_slot_id    INT NOT NULL REFERENCES team_slot(id) ON DELETE CASCADE,
    move_id         INT NOT NULL REFERENCES move(id),
    slot            INT NOT NULL CHECK (slot BETWEEN 1 AND 4),
    PRIMARY KEY (team_slot_id, slot)
);

-- EV spread per stat (max 252 per stat, 510 total — enforced in application layer)
CREATE TABLE team_slot_ev (
    team_slot_id    INT NOT NULL REFERENCES team_slot(id) ON DELETE CASCADE,
    stat_id         INT NOT NULL REFERENCES stat(id),
    value           INT NOT NULL CHECK (value BETWEEN 0 AND 252),
    PRIMARY KEY (team_slot_id, stat_id)
);

-- IV spread per stat (0–31)
CREATE TABLE team_slot_iv (
    team_slot_id    INT NOT NULL REFERENCES team_slot(id) ON DELETE CASCADE,
    stat_id         INT NOT NULL REFERENCES stat(id),
    value           INT NOT NULL CHECK (value BETWEEN 0 AND 31),
    PRIMARY KEY (team_slot_id, stat_id)
);