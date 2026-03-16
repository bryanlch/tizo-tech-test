# SIM — Multi-Branch Inventory Management System

![Angular](https://img.shields.io/badge/Angular-21-red)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3-green)
![Java](https://img.shields.io/badge/Java-21-blue)
![Docker](https://img.shields.io/badge/Docker-enabled-blue)
![CI](https://img.shields.io/badge/CI-GitHub_Actions-black)

A full-stack technical challenge project built to demonstrate production-grade architecture, clean code, and real-world engineering practices across the entire stack.

---

## 1. Project Overview

SIM (Sistema de Inventario Multi-sucursal) is a web application that allows a company to manage product inventory across multiple physical branches. Administrators can:

- Register and manage **branches** (with soft delete and update support)
- Manage the global **product catalog**
- Adjust and query **stock levels** per branch
- Authenticate via **JWT-based auth**

The goal of the technical challenge was to demonstrate architectural thinking, backend design, and a polished frontend experience — not just a CRUD app.

### Key Features

- Multi-branch inventory management
- JWT-based authentication
- Soft-delete strategy for branch lifecycle
- Idempotent inventory updates
- Global API response wrapper
- Centralized exception handling
- Dockerized full-stack environment
- CI pipeline with GitHub Actions

---

## 2. Tech Stack

| Layer              | Technology                                          |
| ------------------ | --------------------------------------------------- |
| **Frontend**       | Angular 21 (standalone components), Tailwind CSS v4 |
| **Backend**        | Java 21, Spring Boot 3, Spring Security, Lombok     |
| **Database**       | H2 (in-memory), Spring Data JPA / Hibernate         |
| **Auth**           | JWT (JSON Web Tokens)                               |
| **Infrastructure** | Docker, Docker Compose, Nginx (prod)                |
| **CI/CD**          | GitHub Actions                                      |

---

## 3. System Architecture

The backend follows a **classic layered architecture**:

```
Controller → Service (Interface + Impl) → Repository → Entity
```

All controllers return a **uniform `ApiResponse<T>`** wrapper, and a **`GlobalExceptionHandler`** intercepts all uncaught exceptions to prevent stack trace exposure. Business rules live exclusively in the service layer, keeping controllers thin.

The frontend follows Angular's **feature-based architecture**:

```
core/         → Guards, interceptors, global services
shared/       → Reusable UI components (Neo Design System)
features/     → One folder per domain (branches, products, inventory, auth)
```

---

## 4. Architecture Diagram

```
┌─────────────────────────────────────┐
│           Browser (Angular)         │
│  ┌──────────────────────────────┐   │
│  │   Nginx / Dev Proxy (:80)    │   │
│  └──────────────┬───────────────┘   │
└─────────────────│───────────────────┘
                  │ /api/*
┌─────────────────▼───────────────────┐
│       Spring Boot API (:8080)       │
│  SecurityFilter → Controller →      │
│  Service → Repository               │
└─────────────────┬───────────────────┘
                  │ JPA / Hibernate
┌─────────────────▼───────────────────┐
│         H2 In-Memory Database       │
└─────────────────────────────────────┘
```

In development, Angular's dev server proxies `/api/*` requests to `http://backend:8080`. In production, Nginx handles this reverse proxy role.

---

## 5. Database Design

H2 is used as an in-memory database, configured entirely through JPA annotations — no raw SQL or migration files required. Switching to PostgreSQL or SQL Server requires only a change to `application.properties`; no business logic is affected.

**Main entities:**

| Entity      | Key Fields                                      |
| ----------- | ----------------------------------------------- |
| `User`      | `id`, `username`, `password` (BCrypt-encoded)   |
| `Branch`    | `id`, `name`, `address`, `status` (soft delete) |
| `Product`   | `id`, `name`, `sku`, `price`, `status`          |
| `Inventory` | `id`, `branch` (FK), `product` (FK), `quantity` |

On startup, `DataLoader` seeds the database with 3 branches, 6 products, and 18 inventory records idempotently (no duplicates on restart).

---

## 6. Backend

### Structure

```
com.digitaltech.sim
├── config/         → DataLoader, SecurityConfig
├── controller/     → REST endpoints
├── dto/            → Request/Response DTOs (validated with Jakarta)
├── exception/      → GlobalExceptionHandler
├── model/          → JPA entities
├── repository/     → Spring Data JPA repositories
├── security/       → JWT filter, CustomUserDetailsService, JwtService
└── service/        → Interfaces + Impl
```

### Layered Architecture

- **Controllers** are thin — they delegate directly to services and return HTTP responses.
- **Services** contain all business logic and validate preconditions (e.g., duplicate names, missing entities).
- **Repositories** use derived Spring Data queries only; no native queries.

### Business Logic Highlights

- **Soft delete**: Branches are deactivated (`status = false`) rather than removed from the database.
- **Idempotent stock adjustment**: Inventory entries are created on first stock assignment; subsequent calls adjust quantities.
- **ApiResponse standard**: Every endpoint returns `{ code, message, data }` with consistent HTTP status codes.

### Authentication

See [Section 9](#9-authentication).

---

## 7. Frontend

### Structure

```
src/app
├── core/           → Interceptors, auth guard, services
├── shared/
│   └── ui/         → Neo Design System components
│       ├── button/
│       ├── card/
│       ├── field/
│       ├── input/
│       └── modal/
└── features/       → branches, products, inventory, auth
```

### Component Architecture

All components are **standalone** (Angular 21), using the new control flow syntax (`@if`, `@for`) and **signals** where reactive state is needed. No `NgModule` is used.

### Design System

A custom **Neo Brutalism** design system (`shared/ui`) provides:

- `neo-button` directive — variant + size system with mechanical hover effect
- `neo-card` + `neo-card-header/body/footer` — slotted card components
- `neo-field` + `neo-input` — form field wrapper with validation display
- `ModalService` — `MatDialog`-style dynamic modal system using `createComponent` + `ApplicationRef`

All styles use **Tailwind CSS v4** `@theme` tokens (`shadow-neo`, `neo-primary`, `neo-black`, etc.).

### Backend Communication

In **development**, Angular's dev proxy (configured in `proxy.conf.json`) forwards all `/api/*` requests to `http://backend:8080`.

In **production**, Nginx serves the Angular SPA and proxies `/api/*` to the backend container on the same Docker network — with zero CORS configuration needed.

---

## 8. API Example

**Get all active branches**

```http
GET /api/branch
Authorization: Bearer <token>
```

**Response `200 OK`:**

```json
{
  "code": 200,
  "message": "Branches retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "Downtown",
      "address": "123 Main Street, Downtown",
      "active": true
    }
  ]
}
```

**Validation error `400 Bad Request`:**

```json
{
  "code": 400,
  "message": "Validation error in request parameters",
  "data": {
    "name": "Branch name is required"
  }
}
```

---

## 9. Authentication

The system uses **stateless JWT authentication** via Spring Security:

1. `POST /api/auth/login` — accepts `{ username, password }`, returns a signed JWT.
2. `POST /api/auth/register` — creates a new user and returns a JWT.
3. All other endpoints require `Authorization: Bearer <token>`.
4. `JwtAuthenticationFilter` validates the token on every request and populates the `SecurityContext`.
5. On first startup, `DataLoader` seeds an **admin user** (`admin` / `admin1234`) automatically.

Token validation uses HMAC-SHA256 with a secret key configurable via `application.properties`.

---

## 10. Project Structure

```
tizo-tech-test/
├── backend/
│   ├── src/main/java/com/digitaltech/sim/
│   ├── src/main/resources/application.properties
│   ├── Dockerfile
│   └── Dockerfile.prod
├── frontend/
│   ├── src/app/
│   ├── proxy.conf.json
│   ├── nginx.conf
│   ├── Dockerfile
│   └── Dockerfile.prod
├── docker-compose.yml          ← development
├── docker-compose.prod.yml     ← production
└── README.md
```

---

## 11. Docker Setup

Two Compose files are provided:

| File                      | Purpose                                      |
| ------------------------- | -------------------------------------------- |
| `docker-compose.yml`      | Development — hot reload, volume mounts      |
| `docker-compose.prod.yml` | Production — multi-stage builds, Nginx proxy |

**Development:**

```bash
docker-compose up -d
```

- Frontend: [http://localhost:4200](http://localhost:4200)
- Backend API: [http://localhost:8080](http://localhost:8080)

**Production:**

```bash
docker-compose -f docker-compose.prod.yml up -d --build
```

- Application available at [http://localhost:80](http://localhost:80)
- All `/api/*` traffic is proxied to the backend internally.

---

## 12. CI/CD Pipeline

A **GitHub Actions** workflow handles continuous integration on every push and pull request to `main`:

```yaml
# .github/workflows/ci.yml  (outline)
on: [push, pull_request]
jobs:
  backend:
    - Checkout code
    - Set up JDK 21
    - Run: mvn clean verify
  frontend:
    - Checkout code
    - Set up Node 20 + pnpm
    - Run: pnpm install && pnpm run build
```

The pipeline ensures both the backend compiles and the Angular production build succeeds before any merge is accepted.

---

## 13. Running the Project

### With Docker (recommended)

```bash
git clone <repo-url>
cd tizo-tech-test
docker-compose up -d
```

Access the app at [http://localhost:4200](http://localhost:4200).  
Login with: `admin` / `admin1234`

### Without Docker

**Backend:**

```bash
cd backend
./mvnw spring-boot:run
```

**Frontend:**

```bash
cd frontend
pnpm install
pnpm run start
```

---

## 14. Technical Decisions

| Decision             | Rationale                                                                               |
| -------------------- | --------------------------------------------------------------------------------------- |
| **Angular 21**       | Standalone components + signals reduce boilerplate without losing structure             |
| **Spring Boot**      | Mature ecosystem, convention over configuration, excellent JPA integration              |
| **H2 in-memory**     | Zero-config database for challenge scope; trivially swappable for any RDBMS             |
| **JWT (stateless)**  | No session storage needed; scales horizontally without sticky sessions                  |
| **Docker Compose**   | Reproducible environments; single command to run the entire stack                       |
| **Neo Brutalism**    | High contrast, zero ambiguity — maximizes user interaction clarity                      |
| **`ApiResponse<T>`** | Uniform response envelope prevents API surprises and simplifies frontend error handling |

---

## 15. Future Improvements

- **Replace H2 with PostgreSQL** — Add `docker-compose.yml` database service; update `application.properties`.
- **Role-based access control (RBAC)** — Introduce `ADMIN` / `VIEWER` roles with method-level security (`@PreAuthorize`).
- **Refresh tokens** — Add a `/api/auth/refresh` endpoint to extend sessions without re-login.
- **Audit logging** — Track who changed what and when using Spring Data Envers or a custom event system.
- **Pagination** — Add `Pageable` to list endpoints for large datasets.
- **E2E tests** — Add Playwright or Cypress tests for critical user flows.

## Design Tradeoffs

This project was developed under a limited time frame (3 days), so several pragmatic tradeoffs were made to prioritize clarity, working functionality, and architectural structure.

### H2 Database vs Production Database

H2 was chosen as an in-memory database to simplify setup and ensure the project can run immediately without additional infrastructure.

For a production environment, the system is designed to easily switch to a persistent database such as **PostgreSQL**, which I am familiar with. Because the application relies entirely on JPA/Hibernate abstractions, changing the database would only require updating the datasource configuration.

### No Database Migration Toolgit

Tools like **Flyway** or **Liquibase** were intentionally omitted to reduce setup complexity and development overhead for the challenge.

In a real production system, a migration tool would be essential for versioning database schema changes and managing deployments safely.

### Simplified Authorization Model

The current implementation focuses on **authentication (JWT)** but does not implement role-based authorization.

This decision was made because role management was not explicitly required in the challenge specification and implementing a full RBAC model would increase scope significantly. A natural extension would be introducing roles such as `ADMIN` and `VIEWER` with method-level security using `@PreAuthorize`.

### Layered Architecture vs Hexagonal Architecture

A **classic layered architecture** was chosen instead of a more complex architecture such as **Hexagonal (Ports & Adapters)**.

While hexagonal architecture provides strong decoupling, it also introduces additional abstraction layers. For a time-constrained challenge, the layered approach provides an excellent balance between **clarity, maintainability, and implementation speed**.

### Angular Modern Patterns

The frontend uses **Angular standalone components and signals** to reduce boilerplate and align with modern Angular development practices. This simplifies module management and keeps the codebase lightweight.

### Testing Scope

Due to time constraints, extensive automated testing was not initially implemented. The main focus was ensuring the system architecture, API structure, and frontend interactions were correctly designed.

Adding **unit tests and integration tests** is planned as a next step.

### Infrastructure Simplicity

The project uses **Docker Compose** to orchestrate services. While orchestration platforms like Kubernetes are more suitable for large-scale production environments, Docker Compose provides a simpler and reliable setup for development and demonstration purposes.

### Ephemeral Data

Because H2 runs in memory, all data is lost when the application restarts. This is acceptable for a technical challenge but would not be appropriate for a production environment, where a persistent database would be required.
