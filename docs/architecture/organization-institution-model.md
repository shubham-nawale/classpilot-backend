# Organization & Institution model

## Architectural intent

This sprint introduces the first actual business domain in the modular monolith: organization and institution management.

The domain structure is intentionally lean and aligned with the repository foundation:

- `com.classpilot.organization` package
- JPA entities for `Organization` and `Institution`
- repository interfaces for persistence
- service layer for validation and permission-aware workflows
- thin REST controllers for the API surface

## Relationship

- one organization owns many institutions
- each institution belongs to exactly one organization
- the parent organization is always resolved from the request URL and validated
- organization code and institution code are normalized and uniqueness-checked

## Why this fits the platform

This keeps the project aligned with the broader multi-tenant SaaS direction without introducing students or finance modules prematurely. The design leaves room for later domain modules and tenant-aware service boundaries.
