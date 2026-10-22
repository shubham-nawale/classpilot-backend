# Contributing to ClassPilot Backend

## Development workflow

1. Create a feature branch from `main`.
2. Keep the change set small and reviewable.
3. Validate the relevant unit and integration tests locally.
4. Update docs when architecture or runtime behavior changes.
5. Raise a pull request with a concise description and validation evidence.

## Local setup

- Java 25
- Docker Desktop or Docker Engine
- Git

Use the commands in `README.md` for setup and local validation.

## Code standards

- Prefer small, modular services and domain-specific packages.
- Keep controllers, DTOs, domain logic, and persistence concerns separated.
- Validate API and security requirements before broad feature work.
- Preserve tenant boundaries and authorization checks.

## Security expectations

- Never commit secrets, certificates, or customer data.
- Keep environment variables in `.env` (not tracked by Git).
- Use Flyway migrations rather than Hibernate auto-updates in production.
