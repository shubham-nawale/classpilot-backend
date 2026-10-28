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

## Troubleshooting: Gradle clean/build fails on Windows

If `./gradlew clean build` fails with a message like:

```text
Unable to delete directory '.../classpilot-framework/build'
Failed to delete some children
```

then the usual cause is a stale Java or Gradle process still holding the generated jar file open on Windows.

### Fix

Run the following from the project root:

```powershell
cd "C:\Users\Shubham\Desktop\Projects\classpilot-backend"
.\gradlew --stop
Get-Process java,gradle -ErrorAction SilentlyContinue | Stop-Process -Force
Remove-Item -Recurse -Force .\classpilot-framework\build -ErrorAction SilentlyContinue
.\gradlew clean build --console=plain
```

### Why this happens

Gradle sometimes keeps a daemon or lock on the framework module’s generated `.jar` while the IDE or an earlier build process is still active. On Windows, that prevents the `build` directory from being deleted during `clean`.

### Project rule for the framework module

The local framework project is a shared library, not a runnable Spring Boot app, so it must stay in library mode:

```gradle
tasks.named('bootJar') {
    enabled = false
}

tasks.named('jar') {
    enabled = true
}
```

This is important because the main application depends on `project(':classpilot-framework')`, and the framework module should not resolve or package a `mainClass` as an executable jar.

## Contract-first API workflow

The backend uses a source-controlled OpenAPI contract at `src/main/openapi/classpilot-openapi.yaml`.

```bash
./gradlew openApiGenerate
```

Generated Spring interfaces are emitted under `build/generated/openapi` and are included in the Java source set during compilation.

This keeps the API contract versioned with the codebase and makes it easier to evolve the backend safely.
