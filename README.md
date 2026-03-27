# 🎮 PokeApp

> A full-stack Pokémon platform built from scratch — own database, own API, own frontend.
> Built as a learning course covering Java 21, Spring Boot 3, PostgreSQL, and React.

---

## 📋 Table of Contents

- [About the Project](#about-the-project)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Database Schema](#database-schema)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Course Modules](#course-modules)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [License](#license)

---

## About the Project

PokeApp is a full-stack Pokémon platform that owns **all its data** — no dependency on external APIs at runtime. The game data (Pokémon, types, moves, abilities, items, natures, evolutions) is imported once from PokéAPI's open-source CSV dataset and stored in our own PostgreSQL database.

On top of that data layer, users can:

- Browse a full **Pokédex** filterable by type, generation, and more
- Build and save **competitive teams** with moves, items, natures, EVs and IVs
- Track their **Pokédex progress** (seen / caught per Pokémon)
- **Favorite** Pokémon and write comments and ratings
- Everything tied to a **user account** with JWT authentication

This project is also documented as a **self-paced course** — every decision is explained, nothing is magic.

---

## Tech Stack

### Backend
| Technology | Version | Purpose |
|---|---|---|
| Java | 21 | Language |
| Spring Boot | 3.3.x | Application framework |
| Spring Security | 6.x | Authentication & authorization |
| Spring Data JPA | 3.3.x | Database access layer |
| Hibernate | 6.x | JPA implementation |
| PostgreSQL | 16 | Relational database |
| Flyway | 10.x | Database migrations |
| JWT (jjwt) | 0.12.x | Token-based auth |
| Lombok | Latest | Boilerplate reduction |
| Maven | 3.9.x | Build tool |

### Frontend *(coming soon)*
| Technology | Purpose |
|---|---|
| React 18 | UI framework |
| Vite | Build tool |
| React Router | Client-side routing |
| Axios | HTTP client |
| TailwindCSS | Styling |

### Infrastructure
| Technology | Purpose |
|---|---|
| Docker + Docker Compose | Local PostgreSQL container |
| GitHub | Version control |

---

## Architecture

The backend follows a **multi-module Maven** architecture with clear separation of concerns:

```
pokeapp/                          ← Parent POM (coordinator)
├── pokeapp-domain/               ← Data layer
│   ├── Entities (JPA)            ← Java classes mapped to DB tables
│   └── Repositories              ← Database queries (Spring Data)
│
├── pokeapp-application/          ← Business logic layer
│   ├── Services                  ← All business rules live here
│   ├── DTOs                      ← Data Transfer Objects (API shapes)
│   └── JWT utilities             ← Token generation & validation
│
└── pokeapp-web/                  ← HTTP layer
    ├── Controllers               ← REST endpoints
    ├── Security config           ← Spring Security setup
    └── Exception handlers        ← Global error handling
```

**Dependency flow:**
```
pokeapp-web  →  pokeapp-application  →  pokeapp-domain  →  PostgreSQL
```

Each module only knows about the module below it. `pokeapp-domain` knows nothing about HTTP or security — it's pure data. This makes the codebase easier to navigate, test, and change.

---

## Database Schema

The database has **27 tables** split into two groups:

### Game Data (14 tables)
Seeded once from PokéAPI's open-source CSV dataset. Never modified by users.

| Table | Description |
|---|---|
| `generation` | The 9 Pokémon game generations |
| `type` | The 18 Pokémon types |
| `type_efficacy` | Type matchup chart (damage multipliers) |
| `stat` | HP, Attack, Defense, Sp. Atk, Sp. Def, Speed |
| `ability` | All Pokémon abilities with effects |
| `move` | All moves with power, PP, accuracy, type |
| `nature` | 25 natures with stat modifiers |
| `item` | Held items and their effects |
| `evolution_chain` | Family tree groupings |
| `evolution` | Individual evolution steps with conditions |
| `pokemon_species` | Species-level data (legendary, mythical, etc.) |
| `pokemon` | Individual Pokémon variants with sprites |
| `pokemon_type` | Many-to-many: Pokémon ↔ types |
| `pokemon_stat` | Base stats per Pokémon |
| `pokemon_ability` | Available abilities per Pokémon |

### User Data (13 tables)
Created and modified by users.

| Table | Description |
|---|---|
| `users` | Accounts with hashed passwords |
| `team` | Competitive teams (name, format, visibility) |
| `team_slot` | 6 slots per team with Pokémon, ability, nature, item |
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

- [Java 21](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [Git](https://git-scm.com/)

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/pokeapp.git
cd pokeapp
```

### 2. Start the database

```bash
docker compose up -d
```

This starts a PostgreSQL 16 container with:
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

Create `pokeapp-web/src/main/resources/application-dev.yml`:

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
    show-sql: true
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
```

> ⚠️ This file is git-ignored — never commit credentials.

### 4. Build and run

```bash
# Compile all modules
mvn compile

# Run the application
mvn spring-boot:run -pl pokeapp-web
```

The API will be available at `http://localhost:8080`.

### 5. Verify

```bash
curl http://localhost:8080/api/health
# → { "status": "UP" }
```

---

## Project Structure

```
pokeapp/
├── docker-compose.yml
├── pom.xml                                          ← Parent POM
│
├── pokeapp-domain/
│   ├── pom.xml
│   └── src/main/java/com/pokeapp/domain/
│       ├── entity/                                  ← JPA entities
│       │   ├── pokemon/
│       │   │   ├── Pokemon.java
│       │   │   ├── PokemonSpecies.java
│       │   │   ├── PokemonType.java
│       │   │   └── PokemonStat.java
│       │   ├── game/
│       │   │   ├── Generation.java
│       │   │   ├── Type.java
│       │   │   ├── Move.java
│       │   │   ├── Ability.java
│       │   │   ├── Nature.java
│       │   │   └── Item.java
│       │   └── user/
│       │       ├── User.java
│       │       ├── Team.java
│       │       ├── TeamSlot.java
│       │       ├── Favorite.java
│       │       ├── PokemonComment.java
│       │       ├── PokemonRating.java
│       │       └── PokedexProgress.java
│       └── repository/                              ← Spring Data repositories
│
├── pokeapp-application/
│   ├── pom.xml
│   └── src/main/java/com/pokeapp/application/
│       ├── service/                                 ← Business logic
│       │   ├── AuthService.java
│       │   ├── PokemonService.java
│       │   ├── TeamService.java
│       │   └── UserService.java
│       ├── dto/                                     ← Data Transfer Objects
│       └── security/                                ← JWT utilities
│
└── pokeapp-web/
    ├── pom.xml
    └── src/main/
        ├── java/com/pokeapp/web/
        │   ├── PokeAppApplication.java              ← Entry point
        │   ├── controller/                          ← REST controllers
        │   │   ├── AuthController.java
        │   │   ├── PokemonController.java
        │   │   └── TeamController.java
        │   ├── config/                              ← Spring Security config
        │   └── exception/                           ← Global error handling
        └── resources/
            ├── application.yml
            ├── application-dev.yml                  ← Git ignored
            └── db/migration/                        ← Flyway SQL files
                ├── V1__create_game_tables.sql
                └── V2__create_user_tables.sql
```

---

## API Endpoints

### Authentication
```
POST   /api/auth/register        Register a new user
POST   /api/auth/login           Login and receive JWT token
GET    /api/auth/me              Get current user profile (protected)
```

### Pokédex
```
GET    /api/pokemon              List all Pokémon (paginated, filterable)
GET    /api/pokemon/{id}         Get full Pokémon detail
GET    /api/pokemon/{id}/evolutions  Get evolution chain
GET    /api/types                List all types
GET    /api/types/{id}           Get type with damage relations
GET    /api/moves                List all moves
GET    /api/moves/{id}           Get move detail
GET    /api/abilities/{id}       Get ability detail
GET    /api/generations          List all generations
```

### Competitive Teams
```
GET    /api/teams                Get current user's teams (protected)
POST   /api/teams                Create a new team (protected)
GET    /api/teams/{id}           Get team detail
PUT    /api/teams/{id}           Update team (protected, owner only)
DELETE /api/teams/{id}           Delete team (protected, owner only)
POST   /api/teams/{id}/slots     Add Pokémon to a slot (protected)
PUT    /api/teams/{id}/slots/{slot}  Update slot (protected)
```

### User Features
```
GET    /api/users/me/favorites          Get favorites (protected)
POST   /api/users/me/favorites/{id}     Add favorite (protected)
DELETE /api/users/me/favorites/{id}     Remove favorite (protected)
GET    /api/users/me/pokedex            Get Pokédex progress (protected)
PUT    /api/users/me/pokedex/{id}       Update seen/caught (protected)
POST   /api/pokemon/{id}/comments       Post a comment (protected)
POST   /api/pokemon/{id}/ratings        Rate a Pokémon (protected)
```

> **Authentication:** Protected routes require `Authorization: Bearer <token>` header.

---

## Course Modules

This project is built as a step-by-step course. Each module explains **what**, **why**, and **how**.

### Phase 1 — Backend

| Module | Topic | Status |
|---|---|---|
| 1.1–1.2 | Maven multi-module structure | ✅ Done |
| 1.3 | Docker + PostgreSQL setup | ✅ Done |
| 1.4 | Spring Boot entry point | ✅ Done |
| 1.5 | Application configuration (YAML) | ✅ Done |
| 1.6 | Flyway migrations — all 27 tables | 🔄 In progress |
| 2.1–2.4 | JPA entities + repositories | ⏳ Pending |
| 3.1–3.6 | Authentication — JWT + Spring Security | ⏳ Pending |
| 4.1–4.4 | CSV data seeder — all Pokémon imported | ⏳ Pending |
| 5.1–5.6 | Pokémon REST controllers + DTOs | ⏳ Pending |
| 6.1–6.4 | Competitive team endpoints | ⏳ Pending |
| 7.1–7.4 | Favorites, comments, ratings, Pokédex | ⏳ Pending |
| 8.1–8.4 | Error handling, Swagger, CORS | ⏳ Pending |

### Phase 2 — Frontend *(coming after backend is complete)*

| Module | Topic | Status |
|---|---|---|
| F1 | Vite + React setup, routing | ⏳ Pending |
| F2 | Auth pages — register, login | ⏳ Pending |
| F3 | Pokédex — list, filters, detail | ⏳ Pending |
| F4 | Team builder UI | ⏳ Pending |
| F5 | User profile — favorites, progress | ⏳ Pending |
| F6 | Comments, ratings, social | ⏳ Pending |
| F7 | Polish — responsive, loading, errors | ⏳ Pending |

### Phase 3 — Extras *(optional)*

| Module | Topic | Status |
|---|---|---|
| E1 | Unit + integration tests (JUnit + Mockito) | ⏳ Pending |
| E2 | Docker — containerize the app | ⏳ Pending |
| E3 | Deploy to cloud (Railway / Render) | ⏳ Pending |

---

## Roadmap

- [x] Project structure and Maven setup
- [x] Docker + PostgreSQL configuration
- [x] Spring Boot entry point and YAML config
- [ ] Flyway database migrations
- [ ] JPA entities and repositories
- [ ] JWT authentication (register / login)
- [ ] Data seeder — import all Pokémon from CSV
- [ ] Full REST API
- [ ] React frontend
- [ ] Tests
- [ ] Docker deployment
- [ ] Cloud hosting

---

## Contributing

This is a personal learning project, but feedback and suggestions are welcome! Feel free to open an issue or reach out.

---

## License

Distributed under the MIT License. See `LICENSE` for more information.

---

> Pokémon and all related names are trademarks of Nintendo / Game Freak.
> This project is for educational purposes only and is not affiliated with or endorsed by Nintendo.
