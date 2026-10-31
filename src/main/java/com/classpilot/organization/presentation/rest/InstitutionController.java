package com.classpilot.organization.presentation.rest;

import com.classpilot.organization.application.dto.CreateInstitutionRequest;
import com.classpilot.organization.application.dto.UpdateInstitutionRequest;
import com.classpilot.organization.application.service.InstitutionService;
import com.classpilot.organization.domain.model.Institution;
import com.classpilot.shared.api.ApiResponse;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class InstitutionController {

    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @PostMapping("/organizations/{organizationId}/institutions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InstitutionResponse>> createInstitution(
            @PathVariable String organizationId,
            @RequestBody CreateInstitutionRequest request,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId) {
        Institution institution = institutionService.createInstitution(organizationId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(toResponse(institution), "Institution created successfully", correlationId));
    }

    @GetMapping("/organizations/{organizationId}/institutions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<InstitutionPageResponse>> listInstitutions(
            @PathVariable String organizationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String direction,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId) {
        Page<Institution> institutions = institutionService.listInstitutions(organizationId, page, size, sortBy,
                direction);
        List<InstitutionResponse> response = institutions.getContent().stream().map(this::toResponse).toList();
        PaginationResponse pagination = new PaginationResponse(
                institutions.getNumber(),
                institutions.getSize(),
                institutions.getTotalElements(),
                institutions.getTotalPages());
        InstitutionPageResponse body = new InstitutionPageResponse(response, pagination);
        return ResponseEntity.ok(new ApiResponse<>(true, "Institutions retrieved successfully", body, Instant.now(),
                correlationId, List.of()));
    }

    @GetMapping("/institutions/{institutionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<InstitutionResponse>> getInstitution(
            @PathVariable String institutionId,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId) {
        Institution institution = institutionService.getInstitution(institutionId);
        return ResponseEntity.ok(
                ApiResponse.success(toResponse(institution), "Institution retrieved successfully", correlationId));
    }

    @PatchMapping("/institutions/{institutionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InstitutionResponse>> updateInstitution(
            @PathVariable String institutionId,
            @RequestBody UpdateInstitutionRequest request,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId) {
        Institution institution = institutionService.getInstitution(institutionId);
        Institution updated = institutionService.updateInstitution(institution.getOrganization().getId(), institutionId,
                request);
        return ResponseEntity.ok(
                ApiResponse.success(toResponse(updated), "Institution updated successfully", correlationId));
    }

    private InstitutionResponse toResponse(Institution institution) {
        return new InstitutionResponse(
                institution.getId(),
                institution.getOrganization().getId(),
                institution.getName(),
                institution.getCode(),
                institution.getInstitutionType().name(),
                institution.getStatus().name(),
                institution.getCreatedAt(),
                institution.getUpdatedAt());
    }

    public record InstitutionResponse(
            String id,
            String organizationId,
            String name,
            String code,
            String institutionType,
            String status,
            Instant createdAt,
            Instant updatedAt) {
    }

    public record InstitutionPageResponse(
            List<InstitutionResponse> data,
            PaginationResponse pagination) {
    }

    public record PaginationResponse(
            int page,
            int size,
            long totalElements,
            int totalPages) {
    }
}
