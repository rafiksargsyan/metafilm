# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repo Structure

```
metafilm/
  services/
    main-ctx/       ← Spring Boot backend (Java 21)
  web-ui/           ← React frontend for end users (TypeScript, Vite)
  admin-ui/         ← React frontend for admins (TypeScript, Vite)
```

Each sub-project has its own `CLAUDE.md` with commands and architecture details.

## What This Is

Metafilm is a movie/TV-show metadata platform (similar to TMDB/IMDB). It stores and serves rich metadata for movies and TV shows, periodically synced/refreshed from external sources.

## Auth Model

Two separate Firebase projects:

1. **End users** — standard Firebase project. Frontend sends `Authorization: Bearer <token>` + `X-ACCOUNT-ID: <accountId>`. Backend validates against end-user Firebase issuer.
2. **Admins** — separate Firebase project. Admin UI sends `Authorization: Bearer <token>`. Backend validates against admin Firebase issuer (`/admin/**` endpoints only). Stateless — no `AdminProfile` table; any JWT from the admin Firebase project is trusted.

All admin REST endpoints are under the `/admin` URL prefix.
