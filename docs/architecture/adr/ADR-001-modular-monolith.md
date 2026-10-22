# ADR-001: Modular monolith

- Status: Accepted
- Date: 2026-10-22

## Context

ClassPilot is a multi-tenant SaaS platform for education businesses. The immediate priority is a stable backend foundation with a clean structure and secure tenant-aware architecture.

## Decision

Use a modular monolith with package-level separation rather than a microservice architecture. Initial modules will be organized around business domains such as `identity`, `tenancy`, `student`, and later `fee`, `attendance`, and `academic`.

## Consequences

- Simpler deployment and local development
- Easier single-database transactional boundaries
- Clear domain boundaries that can evolve later into microservices if needed
- Requires disciplined package design and explicit use of tenancy checks
