# Organization & Institution schema

## Tables

### organizations

| Column | Type | Notes |
|---|---|---|
| id | VARCHAR(36) PK | UUID string |
| name | VARCHAR(150) | required |
| code | VARCHAR(50) | unique, normalized |
| status | VARCHAR(20) | ACTIVE / SUSPENDED / INACTIVE |
| created_at | TIMESTAMP | created automatically |
| updated_at | TIMESTAMP | updated automatically |
| version | BIGINT | optimistic locking |

### institutions

| Column | Type | Notes |
|---|---|---|
| id | VARCHAR(36) PK | UUID string |
| organization_id | VARCHAR(36) FK | references organizations.id |
| name | VARCHAR(150) | required |
| code | VARCHAR(50) | required |
| institution_type | VARCHAR(20) | SCHOOL / COACHING / OTHER |
| status | VARCHAR(20) | ACTIVE / SUSPENDED / INACTIVE |
| created_at | TIMESTAMP | created automatically |
| updated_at | TIMESTAMP | updated automatically |
| version | BIGINT | optimistic locking |

## Constraints

- `organizations.code` is unique
- `institutions(organization_id, code)` is unique
- `institutions.organization_id` has a foreign key to `organizations`
- `institutions` has a query index on `organization_id`

## Migration

The schema is created by Flyway migrations under `src/main/resources/db/migration`. V2 converts the legacy numeric IDs created by V1 to UUID-compatible strings, preserving existing ID values as strings. V1 remains unchanged so Flyway checksum validation continues to succeed for databases where it has already been applied.

The project intentionally avoids Hibernate auto schema updates in production, keeping Flyway as the source of truth.
