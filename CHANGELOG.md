# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]

### Added
- Repository and engineering foundation for ClassPilot backend
- Java 25, Spring Boot, Gradle, Flyway, MySQL, and OpenAPI foundation
- Docker Compose for local MySQL development
- CI workflow for build and test validation
- Health-check startup application and documentation
- Organization and institution domain model, persistence layer, service layer, and REST controllers
- Flyway migration for `organizations` and `institutions`
- Documentation for API contract, schema, tenant isolation, architectural model, and sprint plan

### Changed
- Updated the project OpenAPI contract for the first real business domain: organization and institution management
- Kept the existing JWT/auth foundation and response-envelope conventions while extending the API surface
