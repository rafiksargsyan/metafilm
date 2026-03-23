# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./gradlew build

# Run tests
./gradlew test

# Run a single test class
./gradlew test --tests com.rsargsyan.metafilm.main_ctx.SomeTestClass

# Run the application
./gradlew bootRun

# Clean
./gradlew clean
```

## Architecture

**Spring Boot 3.5.7 / Java 21** microservice following **hexagonal architecture (ports & adapters)** under `src/main/java/com/rsargsyan/metafilm/main_ctx/`:

```
adapters/driving/       ← Inbound: REST controllers, security filters
adapters/driven/        ← Outbound: repository implementations (JPA)
core/
  app/                  ← Application services, DTOs
  domain/               ← Aggregates, value objects
  ports/repository/     ← Repository interfaces
  exception/            ← Domain exceptions
```

### Request Flow

```
HTTP → Security Filters (JWT) → Controllers → Application Services → Domain Model
                                                                    → Repository Ports → JPA
```

### Authentication

Two security filter chains, each with its own `JwtDecoder`:

1. **`/admin/**`** — `adminSecurityFilterChain` (Order 1) validates JWT against admin Firebase project. Stateless: no DB lookup for admin users.
2. **All other routes** — `userSecurityFilterChain` (Order 2) validates JWT against end-user Firebase project. `UserContextInterceptor` resolves `UserProfile` from DB using `sub` claim + `X-ACCOUNT-ID` header.

`UserContextHolder` (ThreadLocal) carries `UserContext` (includes `isAdmin` flag) through the request.

### Key Domain Aggregates

- `Account` — root ownership container for end users
- `UserProfile` — user with name, scoped to Account
- `Principal` — external Firebase identity (`sub` claim), linked to UserProfile

All aggregates use TSID-based Long primary keys (`@Tsid` from Hypersistence).

### REST Endpoints

| Method | Path | Auth |
|--------|------|------|
| POST | `/user/signup-external` | End-user JWT |
| GET/POST | `/admin/**` | Admin JWT |

### Environment Variables

```
SPRING_DATASOURCE_URL / USERNAME / PASSWORD
FIREBASE_PROJECT_ID           ← end-user Firebase project
ADMIN_FIREBASE_PROJECT_ID     ← admin Firebase project
```
