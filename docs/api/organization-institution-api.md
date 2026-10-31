# Organization & Institution API

## Scope

This sprint exposes the first business-facing APIs for organization and institution management. The contract is grounded in `src/main/openapi/classpilot-openapi.yaml` and keeps the existing response envelope and JWT-authenticated access model intact.

## Endpoints

### Organization endpoints

- `POST /api/v1/organizations`
  - creates a new organization
  - requires authenticated platform admin
- `GET /api/v1/organizations/{organizationId}`
  - returns a single organization
- `PATCH /api/v1/organizations/{organizationId}`
  - updates the organization name

### Institution endpoints

- `POST /api/v1/organizations/{organizationId}/institutions`
  - creates an institution under a specific organization
- `GET /api/v1/organizations/{organizationId}/institutions`
  - paginated institution listing for an organization
- `GET /api/v1/institutions/{institutionId}`
  - fetches a single institution
- `PATCH /api/v1/institutions/{institutionId}`
  - updates the institution name

## Common response format

```json
{
  "success": true,
  "message": "Institution created successfully",
  "data": {
    "id": "inst-001",
    "organizationId": "org-001",
    "name": "Sunrise Public School",
    "code": "SUNRISE-PUBLIC",
    "institutionType": "SCHOOL",
    "status": "ACTIVE",
    "createdAt": "2026-10-28T10:00:00Z",
    "updatedAt": "2026-10-28T10:00:00Z"
  },
  "timestamp": "2026-10-28T10:00:00Z",
  "correlationId": "7af5fd42-74ca-4632-af9d-91d06c7d5559",
  "errors": []
}
```

## Pagination contract

The list endpoint includes a `pagination` object beside the `data` list:

```json
{
  "success": true,
  "message": "Institutions retrieved successfully",
  "data": [
    { "id": "inst-001", "name": "Sunrise Public School" }
  ],
  "pagination": {
    "page": 0,
    "size": 20,
    "totalElements": 1,
    "totalPages": 1
  },
  "timestamp": "2026-10-28T10:00:00Z",
  "correlationId": "...",
  "errors": []
}
```

## Validation rules

- names must be non-blank and 2-150 chars
- codes must be 2-50 chars and normalized to uppercase
- duplicate organization codes are rejected
- duplicate institution codes are rejected within the same organization
- pagination size is capped at 100
- unsupported sort fields return `400`

## Security

- login remains public
- protected routes require JWT bearer auth
- organization and institution creation/update permissions are enforced in the service/controller layer
- cross-organization access checks are required before returning any record
