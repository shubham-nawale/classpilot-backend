# ClassPilot Backend

ClassPilot is a configurable, multi-tenant SaaS platform for schools and education organizations. This repository contains the backend foundation for the modular monolith architecture that will later serve a web/mobile front end.

## Repository metadata

- Name: `classpilot-backend`
- Language: Java 25
- Framework: Spring Boot 3.5.x
- Build tool: Gradle with Kotlin DSL
- Persistence: MySQL 8.x + Flyway
- API documentation: springdoc-openapi + Swagger UI
- Tests: JUnit 5, Mockito, Spring Boot Test, Testcontainers

## Why this foundation

This first increment creates the engineering foundation, not business domain modules. It intentionally focuses on a secure, verifiable platform design so later features can build safely on a stable base.

## Prerequisites

- Java 25 JDK
- Docker Desktop or Docker Engine
- Git

## Local setup

```bash
./gradlew clean test
./gradlew bootRun --args='--spring.profiles.active=local'
```

For MySQL locally:

```bash
cp .env.example .env
# edit .env with local values if needed
docker compose up -d mysql
```

Then open:

- Swagger UI: http://localhost:8080/swagger-ui.html
- Actuator health: http://localhost:8080/actuator/health

## Project layout

```text
classpilot-backend/
├── .github/workflows
├── docs/
├── gradle/
├── src/
├── .env.example
├── .gitignore
├── build.gradle.kts
├── CHANGELOG.md
├── compose.yaml
├── CONTRIBUTING.md
├── Dockerfile
├── README.md
├── settings.gradle.kts
└── gradlew
```

## Architecture decisions

See the ADRs under `docs/architecture/adr/`.

## Security note

The application is not yet exposing full tenant-scoped business APIs. The next increments will add identity, security, and tenant isolation before production-facing student or financial data is introduced.
