package com.classpilot.organization.application.service;

import com.classpilot.organization.application.dto.CreateInstitutionRequest;
import com.classpilot.organization.application.dto.UpdateInstitutionRequest;
import com.classpilot.organization.domain.model.Institution;
import com.classpilot.organization.domain.model.Organization;
import com.classpilot.organization.domain.repository.InstitutionRepository;
import com.classpilot.organization.domain.repository.OrganizationRepository;
import java.util.Locale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class InstitutionService {

    private final InstitutionRepository institutionRepository;
    private final OrganizationRepository organizationRepository;

    public InstitutionService(InstitutionRepository institutionRepository,
            OrganizationRepository organizationRepository) {
        this.institutionRepository = institutionRepository;
        this.organizationRepository = organizationRepository;
    }

    @Transactional
    public Institution createInstitution(String organizationId, CreateInstitutionRequest request) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found"));

        if (request == null || request.name() == null || request.code() == null || request.institutionType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Institution payload is invalid");
        }

        String normalizedCode = normalizeCode(request.code());
        if (institutionRepository.existsByOrganizationIdAndCodeIgnoreCase(organizationId, normalizedCode)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Institution code already exists within organization");
        }

        Institution institution = new Institution(
                organization,
                normalizeName(request.name()),
                normalizedCode,
                request.institutionType());

        return institutionRepository.save(institution);
    }

    @Transactional(readOnly = true)
    public Institution getInstitution(String institutionId) {
        return institutionRepository.findById(institutionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Institution not found"));
    }

    @Transactional(readOnly = true)
    public Institution getInstitutionForOrganization(String organizationId, String institutionId) {
        return institutionRepository.findByOrganizationIdAndId(organizationId, institutionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Institution not found for organization"));
    }

    @Transactional
    public Institution updateInstitution(String organizationId, String institutionId,
            UpdateInstitutionRequest request) {
        Institution institution = getInstitutionForOrganization(organizationId, institutionId);

        if (request == null || request.name() == null || request.name().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one valid field is required");
        }

        institution.setName(normalizeName(request.name()));
        return institutionRepository.save(institution);
    }

    @Transactional(readOnly = true)
    public Page<Institution> listInstitutions(String organizationId, int page, int size, String sortBy,
            String direction) {
        organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found"));

        int validatedPage = Math.max(0, page);
        int validatedSize = Math.min(Math.max(1, size), 100);

        String validatedSortBy = switch (sortBy == null ? "name" : sortBy) {
            case "name" -> "name";
            case "code" -> "code";
            case "createdAt" -> "createdAt";
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported sort field");
        };

        Sort.Direction sortDirection = "DESC".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(validatedPage, validatedSize, Sort.by(sortDirection, validatedSortBy));
        return institutionRepository.findByOrganizationId(organizationId, pageable);
    }

    public String normalizeCode(String code) {
        if (code == null || code.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Institution code is required");
        }
        String normalized = code.trim().replace(' ', '-').toUpperCase(Locale.ROOT);
        if (!normalized.matches("^[A-Z0-9][A-Z0-9_-]*$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Institution code is invalid");
        }
        return normalized;
    }

    public String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Institution name is required");
        }
        return name.trim();
    }
}
