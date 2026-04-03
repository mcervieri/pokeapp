# PokeApp

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.4-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![React](https://img.shields.io/badge/React-18-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Multi--module-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)
![Backend](https://img.shields.io/badge/Backend-Complete-brightgreen?style=for-the-badge)
![Frontend](https://img.shields.io/badge/Frontend-In_Progress-orange?style=for-the-badge)

> A full-stack competitive Pokémon platform built from scratch — own database, own API, own frontend.
> Built as a structured learning course covering Java 21, Spring Boot 3, PostgreSQL, and React.

---

## Table of Contents

- [About the Project](#about-the-project)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Database Schema](#database-schema)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Course Modules](#course-modules)
- [Roadmap](#roadmap)
- [Future — TCG Modules](#future--tcg-modules)
- [License](#license)

---

## About the Project

PokeApp is a full-stack Pokémon platform that owns **all its data** — no dependency on external APIs at runtime. Game data (Pokémon, types, moves, abilities, items, natures, evolutions) is imported once from PokéAPI's open-source CSV dataset and stored in our own PostgreSQL database.

On top of that data layer, users can:

- Browse a full **Pokédex** filterable by type, generation, and more
- Build and save **competitive teams** with moves, items, natures, EVs and IVs
- Track their **Pokédex progress** (seen / caught per Pokémon)
- **Favorite** Pokémon and write comments and ratings
- Everything tied to a **user account** with JWT authentication

This project is also documented as a **self-paced course** — every architectural decision is explained before code is written. Nothing is magic.

---

## Tech Stack

### Backend

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.4-6DB33F?style=flat-square&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6.x-6DB33F?style=flat-square&logo=spring-security&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-3.3.x-6DB33F?style=flat-square&logo=spring&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-6.x-59666C?style=flat-square&logo=hibernate&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?style=flat-square&logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-10.x-CC0200?style=flat-square&logo=flyway&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-jjwt_0.12.x-000000?style=flat-square&logo=json-web-tokens&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-latest-pink?style=flat-square)
![Maven](https://img.shields.io/badge/Maven-3.9.x-C71A36?style=flat-square&logo=apache-maven&logoColor=white)

### Frontend *(in progress)*

![React](https://img.shields.io/badge/React-18-61DAFB?style=flat-square&logo=react&logoColor=black)
![Vite](https://img.shields.io/badge/Vite-5.x-646CFF?style=flat-square&logo=vite&logoColor=white)
![React Router](https://img.shields.io/badge/React_Router-6.x-CA4245?style=flat-square&logo=react-router&logoColor=white)
![Axios](https://img.shields.io/badge/Axios-1.x-5A29E4?style=flat-square&logo=axios&logoColor=white)
![TailwindCSS](https://img.shields.io/badge/TailwindCSS-3.x-06B6D4?style=flat-square&logo=tailwind-css&logoColor=white)

### Infrastructure

![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=flat-square&logo=docker&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-Version_Control-181717?style=flat-square&logo=github&logoColor=white)

---

## Architecture

The backend follows a **Maven multi-module** architecture with strict separation of concerns:

```
pokeapp/                          ← Parent POM (coordinator)
├── pokeapp-domain/               ← Data layer
│   ├── Entities (JPA)            ← Java classes mapped to DB tables
│   └── Repositories              ← Database queries (Spring Data)
│
├── pokeapp-application/          ← Business logic layer
│   ├── Services                  ← All business rules live here
│   ├── DTOs                      ← Data Transfer Objects (API shapes)
│   └── Security utilities        ← JWT generation & validation
│
└── pokeapp-web/                  ← HTTP layer
    ├── Controllers               ← REST endpoints
    ├── Security config           ← Spring Security + JWT filter
    └── Exception handlers        ← Global error handling (@ControllerAdvice)
```

**Dependency flow (one direction only):**

```
pokeapp-web  →  pokeapp-application  →  pokeapp-domain  →  PostgreSQL
```

Each module only knows about the module below it. `pokeapp-domain` knows nothing about HTTP or security — it's pure data. This makes the codebase easier to navigate, test, and extend.

---

## Database Schema

27 tables across two groups, managed by Flyway migrations (V1–V6).

### Game Data (14 tables)

Seeded once from PokéAPI's open-source CSV dataset. Never modified by users.

| Table | Description |
|---|---|
| `generation` | The 9 Pokémon game generations |
| `type` | The 18 Pokémon types |
| `type_efficacy` | Type matchup chart with damage multipliers |
| `stat` | HP, Attack, Defense, Sp. Atk, Sp. Def, Speed |
| `ability` | All Pokémon abilities with effect text |
| `move` | All moves — power, PP, accuracy, type, damage class |
| `nature` | 25 natures with stat modifiers |
| `item` | Held items with effect text and sprite URLs |
| `evolution_chain` | Evolution family tree groupings |
| `evolution` | Individual evolution steps with conditions |
| `pokemon_species` | Species-level data (legendary, mythical, etc.) |
| `pokemon` | Individual Pokémon variants with sprite URLs |
| `pokemon_type` | Many-to-many: Pokémon ↔ types |
| `pokemon_stat` | Base stats per Pokémon |
| `pokemon_ability` | Available abilities per Pokémon (including hidden) |
| `pokemon_move` | Learnable moves per Pokémon per learn method |

### User Data (13 tables)

Created and modified at runtime by registered users.

| Table | Description |
|---|---|
| `users` | Accounts with BCrypt-hashed passwords |
| `team` | Competitive teams (name, format, visibility) |
| `team_slot` | Up to 6 Pokémon slots per team |
| `team_slot_move` | Up to 4 moves per slot |
| `team_slot_ev` | EV values per stat per slot |
| `team_slot_iv` | IV values per stat per slot |
| `favorite` | User ↔ Pokémon bookmarks |
| `pokemon_comment` | User comments on Pokémon |
| `pokemon_rating` | User ratings (1–5) on Pokémon |
| `pokedex_progress` | Seen / caught tracking per user per Pokémon |

---

## Getting Started

### Prerequisites

![Java](https://img.shields.io/badge/Java-21+-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36?style=flat-square&logo=apache-maven&logoColor=white)
![Docker](https://img.shields.io/badge/Docker_Desktop-required-2496ED?style=flat-square&logo=docker&logoColor=white)
![Git](https://img.shields.io/badge/Git-required-F05032?style=flat-square&logo=git&logoColor=white)

### 1. Clone the repository

```bash
git clone https://github.com/mcervieri/pokeapp.git
cd pokeapp
```

### 2. Start the database

```bash
docker compose up -d
```

This starts PostgreSQL 16 with:

- **Host:** `localhost:5432`
- **Database:** `pokeapp_db`
- **User:** `pokeapp_user`
- **Password:** `pokeapp_pass`

Verify it's running:

```bash
docker ps
docker exec -it pokeapp-postgres pg_isready -U pokeapp_user -d pokeapp_db
```

### 3. Configure the application

Create `pokeapp-web/src/main/resources/application-dev.yml` (this file is `.gitignore`d):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/pokeapp_db
    username: pokeapp_user
    password: pokeapp_pass
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 10
      minimum-idle: 2
      connection-timeout: 30000
  jpa:
    show-sql: false
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true

app:
  jwt:
    secret: your-secret-key-here
    expiration: 86400000

seed:
  enabled: true
```

### 4. Build and run

```bash
# Compile all modules
mvn compile

# Run the application
mvn spring-boot:run -pl pokeapp-web -am
```

The API will be available at `http://localhost:8080`.

### 5. Seed the database

On first run with `seed.enabled=true`, the `DataSeederService` will import all Pokémon data from the bundled CSV files. This runs once and is idempotent — if data already exists, seeding is skipped.

---

## Project Structure

```
pokeapp/
├── docker-compose.yml
├── pom.xml                                           ← Parent POM
│
├── pokeapp-domain/
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/pokeapp/domain/
│       │   ├── entity/                               ← 21 JPA entities
│       │   │   ├── Ability.java
│       │   │   ├── DamageClass.java
│       │   │   ├── Evolution.java
│       │   │   ├── EvolutionChain.java
│       │   │   ├── Favorite.java
│       │   │   ├── Generation.java
│       │   │   ├── Item.java
│       │   │   ├── Move.java
│       │   │   ├── Nature.java
│       │   │   ├── PokedexProgress.java
│       │   │   ├── Pokemon.java
│       │   │   ├── PokemonAbility.java
│       │   │   ├── PokemonComment.java
│       │   │   ├── PokemonMove.java
│       │   │   ├── PokemonRating.java
│       │   │   ├── PokemonSpecies.java
│       │   │   ├── PokemonStat.java
│       │   │   ├── PokemonType.java
│       │   │   ├── Stat.java
│       │   │   ├── Team.java
│       │   │   ├── TeamSlot.java
│       │   │   ├── Type.java
│       │   │   ├── TypeEfficacy.java
│       │   │   └── User.java
│       │   └── repository/                           ← Spring Data JPA repositories
│       └── test/                                     ← Repository integration tests
│
├── pokeapp-application/
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/pokeapp/application/
│       │   ├── dto/                                  ← 8 DTO records
│       │   │   ├── AbilityDto.java
│       │   │   ├── ItemDto.java
│       │   │   ├── MoveDto.java
│       │   │   ├── NatureDto.java
│       │   │   ├── PokemonDetailDto.java
│       │   │   ├── PokemonSummaryDto.java
│       │   │   ├── TypeDto.java
│       │   │   └── UserDto.java
│       │   ├── service/                              ← 6 service classes
│       │   │   ├── AbilityService.java
│       │   │   ├── AuthService.java
│       │   │   ├── ItemService.java
│       │   │   ├── MoveService.java
│       │   │   ├── NatureService.java
│       │   │   ├── PokemonService.java
│       │   │   └── TypeService.java
│       │   └── security/                             ← JWT utilities
│       │       ├── JwtService.java
│       │       └── UserDetailsServiceImpl.java
│       └── test/                                     ← Service unit tests
│
└── pokeapp-web/
    ├── pom.xml
    └── src/
        ├── main/
        │   ├── java/com/pokeapp/web/
        │   │   ├── PokeAppApplication.java            ← Entry point
        │   │   ├── controller/                        ← 6 REST controllers
        │   │   │   ├── AbilityController.java
        │   │   │   ├── AuthController.java
        │   │   │   ├── ItemController.java
        │   │   │   ├── MoveController.java
        │   │   │   ├── NatureController.java
        │   │   │   ├── PokemonController.java
        │   │   │   └── TypeController.java
        │   │   ├── config/
        │   │   │   └── SecurityConfig.java
        │   │   └── filter/
        │   │       └── JwtAuthFilter.java
        │   └── resources/
        │       ├── application.yml
        │       ├── application-dev.yml                ← Git-ignored
        │       └── db/migration/                      ← Flyway SQL (V1–V6)
        │           ├── V1__create_generation_type.sql
        │           ├── V2__create_pokemon_tables.sql
        │           ├── V3__create_move_ability.sql
        │           ├── V4__create_item_nature.sql
        │           ├── V5__create_user_tables.sql
        │           └── V6__create_team_tables.sql
        └── test/                                      ← Controller tests (@WebMvcTest)
            └── PokemonControllerTest.java
```

---

## API Endpoints

### Authentication (public)

```
POST  /api/auth/register    Register a new user
POST  /api/auth/login       Login and receive a JWT token
GET   /api/auth/me          Get current user profile  [protected]
```

### Pokédex (public)

```
GET   /api/pokemon              List all Pokémon (paginated, filterable)
GET   /api/pokemon/{id}         Get full Pokémon detail
GET   /api/types                List all types
GET   /api/types/{id}           Get type detail
GET   /api/moves                List all moves (paginated)
GET   /api/moves/{id}           Get move detail
GET   /api/abilities            List all abilities
GET   /api/abilities/{id}       Get ability detail
GET   /api/natures              List all natures
GET   /api/items                List all items
GET   /api/items/{id}           Get item detail
```

### Competitive Teams (protected)

```
GET    /api/teams                      Get current user's teams
POST   /api/teams                      Create a new team
GET    /api/teams/{id}                 Get team detail
PUT    /api/teams/{id}                 Update team (owner only)
DELETE /api/teams/{id}                 Delete team (owner only)
POST   /api/teams/{id}/slots           Add Pokémon to a slot
PUT    /api/teams/{id}/slots/{slot}    Update a slot
DELETE /api/teams/{id}/slots/{slot}    Remove a slot
```

### User Features (protected)

```
GET    /api/users/me/favorites          List favorites
POST   /api/users/me/favorites/{id}     Add favorite
DELETE /api/users/me/favorites/{id}     Remove favorite

GET    /api/users/me/pokedex            Get Pokédex progress
PUT    /api/users/me/pokedex/{id}       Mark seen / caught

POST   /api/pokemon/{id}/comments       Post a comment
POST   /api/pokemon/{id}/ratings        Rate a Pokémon (1–5)
```

> **Authentication:** All protected routes require `Authorization: Bearer <token>` in the request header.

---

## Course Modules

This project is built as a self-paced course. Each lesson follows a **what → why → how → verify** format.

### Phase 1 — Backend (Complete)

| Module | Topic | Status |
|---|---|---|
| 1 | Maven multi-module structure | ![Done](https://img.shields.io/badge/Done-brightgreen?style=flat-square) |
| 2 | Docker + PostgreSQL setup | ![Done](https://img.shields.io/badge/Done-brightgreen?style=flat-square) |
| 3 | Flyway migrations — 27 tables, V1–V6 | ![Done](https://img.shields.io/badge/Done-brightgreen?style=flat-square) |
| 4 | JPA entities — all 21 entities + composite keys | ![Done](https://img.shields.io/badge/Done-brightgreen?style=flat-square) |
| 5 | Spring Data repositories | ![Done](https://img.shields.io/badge/Done-brightgreen?style=flat-square) |
| 6 | JWT authentication — Spring Security 6, filter chain | ![Done](https://img.shields.io/badge/Done-brightgreen?style=flat-square) |
| 7 | Data seeder — 15 CSV files, 16 tables, FK-safe order | ![Done](https://img.shields.io/badge/Done-brightgreen?style=flat-square) |
| 8 | REST API — DTOs, services, 6 controllers, pagination, global exception handling | ![Done](https://img.shields.io/badge/Done-brightgreen?style=flat-square) |

### 🔄 Phase 2 — Frontend (In Progress)

| Module | Topic | Status |
|---|---|---|
| F1 | Vite + React setup, routing | ![Pending](https://img.shields.io/badge/Pending-lightgrey?style=flat-square) |
| F2 | Auth pages — register, login, JWT storage | ![Pending](https://img.shields.io/badge/Pending-lightgrey?style=flat-square) |
| F3 | Pokédex — list, filters, detail page | ![Pending](https://img.shields.io/badge/Pending-lightgrey?style=flat-square) |
| F4 | Team builder UI | ![Pending](https://img.shields.io/badge/Pending-lightgrey?style=flat-square) |
| F5 | User profile — favorites, Pokédex progress | ![Pending](https://img.shields.io/badge/Pending-lightgrey?style=flat-square) |
| F6 | Comments, ratings, social features | ![Pending](https://img.shields.io/badge/Pending-lightgrey?style=flat-square) |
| F7 | Polish — responsive design, loading states, error handling | ![Pending](https://img.shields.io/badge/Pending-lightgrey?style=flat-square) |

### 🔮 Phase 3 — Extras (Planned)

| Module | Topic | Status |
|---|---|---|
| E1 | Docker — containerize the full application | ![Pending](https://img.shields.io/badge/Pending-lightgrey?style=flat-square) |
| E2 | Cloud deployment (Railway / Render) | ![Pending](https://img.shields.io/badge/Pending-lightgrey?style=flat-square) |
| E3 | TCG module | ![Pending](https://img.shields.io/badge/Pending-lightgrey?style=flat-square) |
| E4 | TCG Pocket module | ![Pending](https://img.shields.io/badge/Pending-lightgrey?style=flat-square) |

---

## Roadmap

- [x] Maven multi-module structure
- [x] Docker + PostgreSQL
- [x] Flyway database migrations (27 tables)
- [x] JPA entities and repositories
- [x] JWT authentication (Spring Security 6)
- [x] Data seeder — all Pokémon imported from CSV
- [x] Full REST API — DTOs, services, controllers, pagination, global exception handling
- [ ] React frontend
- [ ] Docker containerization
- [ ] Cloud hosting
- [ ] TCG module (physical card game + deck builder)
- [ ] TCG Pocket module (digital cards + collection tracker)
- [ ] VGC Damage calculator
- [ ] VGC Tournaments results and stats

---

## Future — TCG Modules

![Planned](https://img.shields.io/badge/Status-Planned-blueviolet?style=for-the-badge)
![Self-contained](https://img.shields.io/badge/Data-100%25_Own_Database-2ea44f?style=for-the-badge)

Just like the main game module, **both TCG modules own their data entirely** — no external API at runtime. Card data will be seeded directly from open community datasets into our own PostgreSQL database.

### Why two modules?

TCG and TCG Pocket are fundamentally different games:

| | TCG (Physical) | TCG Pocket (Digital) |
|---|---|---|
| Deck size | 60 cards | 20 cards |
| Energy | Energy cards attached | 1 energy per turn, no energy cards |
| Card subtypes | Basic, Stage 1, Stage 2, EX, GX, V, VMAX, VSTAR... | Basic, Stage 1, Stage 2, ex |
| Retreat | Energy cost | No retreat cost |
| Card source | Physical sets since 1996 | Digital packs since 2024 |
| User feature | Deck builder | Collection tracker + Wonder Pick |

### `pokeapp-tcg` — Physical Card Game

Covers every expansion from Base Set (1996) to the latest Scarlet & Violet sets, with a full 60-card deck builder and legality validation.

**Planned tables:** `tcg_series`, `tcg_set`, `tcg_card`, `tcg_attack`, `tcg_ability`, `tcg_card_weakness`, `tcg_card_resistance`, `tcg_card_legality`, `tcg_deck`, `tcg_deck_card`

### `pokeapp-tcg-pocket` — Digital Card Game

Covers expansions, packs (Charizard, Mewtwo, Pikachu...), Wonder Pick, and a 20-card deck builder with collection tracking.

**Planned tables:** `pocket_expansion`, `pocket_pack`, `pocket_card`, `pocket_attack`, `pocket_ability`, `pocket_card_weakness`, `pocket_card_pack`, `user_pocket_collection`, `user_wonder_pick`, `pocket_deck`, `pocket_deck_card`

### Future architecture

```
pokeapp/
├── pokeapp-domain/          ← Main game data (complete)
├── pokeapp-application/     ← Main game logic (complete)
├── pokeapp-web/             ← Shared HTTP entry point (complete)
├── pokeapp-tcg/             ← Physical TCG (planned)
└── pokeapp-tcg-pocket/      ← Digital TCG Pocket (planned)
```

All modules share the same PostgreSQL database and the same `pokeapp-web` entry point.

---

## License

Distributed under the MIT License. See `LICENSE` for more information.

---

> Pokémon and all related names are trademarks of Nintendo / Game Freak.
> This project is for educational purposes only and is not affiliated with or endorsed by Nintendo.