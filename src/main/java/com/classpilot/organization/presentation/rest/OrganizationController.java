package com.classpilot.organization.presentation.rest;

import com.classpilot.organization.application.dto.CreateOrganizationRequest;
import com.classpilot.organization.application.dto.UpdateOrganizationRequest;
import com.classpilot.organization.application.service.OrganizationService;
import com.classpilot.organization.domain.model.Organization;
import com.classpilot.shared.api.ApiResponse;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping("/organizations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrganizationResponse>> createOrganization(
            @RequestBody CreateOrganizationRequest request,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId) {
        Organization organization = organizationService.createOrganization(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(toResponse(organization), "Organization created successfully",
                        correlationId));
    }

    @GetMapping("/organizations/{organizationId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<OrganizationResponse>> getOrganization(
            @PathVariable String organizationId,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId) {
        Organization organization = organizationService.getOrganization(organizationId);
        return ResponseEntity.ok(
                ApiResponse.success(toResponse(organization), "Organization retrieved successfully", correlationId));
    }

    @PatchMapping("/organizations/{organizationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrganizationResponse>> updateOrganization(
            @PathVariable String organizationId,
            @RequestBody UpdateOrganizationRequest request,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId) {
        Organization organization = organizationService.updateOrganization(organizationId, request);
        return ResponseEntity.ok(
                ApiResponse.success(toResponse(organization), "Organization updated successfully", correlationId));
    }

    private OrganizationResponse toResponse(Organization organization) {
        return new OrganizationResponse(
                organization.getId(),
                organization.getName(),
                organization.getCode(),
                organization.getStatus().name(),
                organization.getCreatedAt(),
                organization.getUpdatedAt());
    }

    public record OrganizationResponse(
            String id,
            String name,
            String code,
            String status,
            Instant createdAt,
            Instant updatedAt) {
    }
}
