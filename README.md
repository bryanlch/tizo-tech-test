# SIM — Multi-Branch Inventory Management System

![Angular](https://img.shields.io/badge/Angular-21-red)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3-green)
![Java](https://img.shields.io/badge/Java-21-blue)
![Docker](https://img.shields.io/badge/Docker-enabled-blue)
![CI](https://img.shields.io/badge/CI-GitHub_Actions-black)
[![Live Demo](https://img.shields.io/badge/Live%20Demo-AWS%20Fargate-orange)](http://3.144.195.249/)

A full-stack technical challenge project built to demonstrate production-grade architecture, clean code, and real-world engineering practices across the entire stack.

> 🌐 **Live instance:** [http://3.144.195.249/](http://3.144.195.249/) — Deployed on AWS ECS Fargate. Login with `admin` / `admin1234`.

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

## 12. CI/CD & Cloud Deployment (AWS)

This project is deployed on **Amazon Web Services (AWS)** using a serverless container architecture. The full lifecycle — from a `git push` to a live update — is automated via **GitHub Actions**.

### Infrastructure

| Component          | Technology                                                                                                |
| ------------------ | --------------------------------------------------------------------------------------------------------- |
| **Compute**        | AWS ECS (Elastic Container Service) on **AWS Fargate** — scales without managing EC2 instances            |
| **Image Registry** | Amazon ECR (Elastic Container Registry) — stores optimized Docker images for both services               |
| **Networking**     | Single public-facing port 80 via Nginx reverse proxy; backend port 8080 is never exposed to the internet  |

**Sidecar Container Pattern:** The Frontend (Angular + Nginx) and Backend (Spring Boot) run together inside the same **ECS Task Definition**.

- **Nginx** exposes port `80` to the outside and routes `/api/*` requests internally to Spring Boot via `localhost:8080`.
- Port `8080` is **not exposed to the internet**, adding an extra security layer.

```
Internet → Public IP :80
              │
          ┌───▼──────────────────────────────────┐
          │        ECS Fargate Task               │
          │  ┌─────────────┐  ┌───────────────┐  │
          │  │  Nginx :80  │──│ Spring Boot   │  │
          │  │  (Angular   │  │    :8080      │  │
          │  │   SPA +     │  │  (REST API)   │  │
          │  │   Proxy)    │  │               │  │
          │  └─────────────┘  └───────────────┘  │
          └──────────────────────────────────────┘
```

### Pipeline Steps

On every push to `main`, the pipeline executes automatically:

1. **Build & Test** — Maven compiles and tests the backend; pnpm builds the Angular production bundle.
2. **Multi-stage Docker Build** — Produces ultra-lightweight Alpine Linux images for both services.
3. **Push to ECR** — Authenticates via IAM roles and pushes the new images to Amazon ECR.
4. **Rolling Update on ECS** — Triggers a **zero-downtime deployment**, replacing the running task with the new version.

```
[Push to main]
      │
      ▼
[GitHub Actions]
      ├─ mvn clean verify            (backend build + tests)
      ├─ pnpm build                  (Angular production build)
      ├─ docker build (multi-stage)  (Alpine-based images)
      ├─ docker push → ECR
      └─ aws ecs update-service → ECS Fargate rolling update
```

### Persistence Strategy

**H2 Database** is configured in **file mode**, not the typical in-memory mode:

- Data persists to the Fargate ephemeral volume for as long as the task is running — unlike `mem` mode, which loses all data on every Spring context restart.
- Reviewers can create records and verify persistence without data loss during hot-reloads.
- Switching to **PostgreSQL** or **Amazon RDS** requires only a datasource configuration change — no business logic is affected.

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

## 14. Technical Decisions & Design Tradeoffs

Built under a ~3-day constraint. Every choice below was made to maximize clarity and working functionality within that scope.

| Decision                    | Rationale & Tradeoff                                                                                              |
| --------------------------- | ----------------------------------------------------------------------------------------------------------------- |
| **Angular 21 (standalone)** | Signals + control flow syntax reduce boilerplate; no `NgModule` needed. Aligns with modern Angular practices.     |
| **Spring Boot**             | Mature ecosystem, convention over configuration, excellent JPA integration.                                        |
| **H2 (file mode)**          | Zero-config DB for challenge scope; trivially swappable (datasource config only). File mode prevents data loss on Spring reloads. |
| **JWT (stateless)**         | No session storage needed; scales horizontally without sticky sessions.                                            |
| **Layered Architecture**    | Better clarity/speed trade-off than Hexagonal (Ports & Adapters) for a time-constrained challenge.               |
| **Docker Compose**          | Reproducible local environment with a single command; Kubernetes would be overkill here.                          |
| **Neo Brutalism UI**        | High contrast, zero visual ambiguity — maximizes interaction clarity.                                              |
| **`ApiResponse<T>`**        | Uniform envelope prevents API surprises and simplifies frontend error handling.                                    |
| **No migration tool**       | Flyway/Liquibase omitted to reduce setup overhead; would be essential in a production environment.                 |
| **No RBAC**                 | Authentication is fully implemented; role-based authorization was out of scope for the challenge spec.             |
| **No automated tests**      | Architecture and API correctness were the priority; unit/integration tests are the natural next step.             |

### Future Improvements

- **Replace H2 with PostgreSQL** — Add a DB service to `docker-compose.yml`; update `application.properties`.
- **Role-based access control (RBAC)** — `ADMIN` / `VIEWER` roles with `@PreAuthorize` method security.
- **Refresh tokens** — `/api/auth/refresh` endpoint to extend sessions without re-login.
- **Audit logging** — Track changes using Spring Data Envers or a custom event system.
- **Pagination** — `Pageable` on all list endpoints for large datasets.
- **Database migrations** — Flyway or Liquibase for production-grade schema versioning.
- **E2E tests** — Playwright or Cypress for critical user flows.
