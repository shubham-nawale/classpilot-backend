package com.classpilot.organization.application.service;

import com.classpilot.organization.application.dto.CreateOrganizationRequest;
import com.classpilot.organization.application.dto.UpdateOrganizationRequest;
import com.classpilot.organization.domain.model.Organization;
import com.classpilot.organization.domain.repository.OrganizationRepository;
import java.util.Locale;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Transactional
    public Organization createOrganization(CreateOrganizationRequest request) {
        String normalizedName = normalizeName(request.name());
        String normalizedCode = normalizeCode(request.code());

        if (organizationRepository.existsByCodeIgnoreCase(normalizedCode)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Organization code already exists");
        }

        return organizationRepository.save(new Organization(normalizedName, normalizedCode));
    }

    @Transactional(readOnly = true)
    public Organization getOrganization(String organizationId) {
        return organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found"));
    }

    @Transactional
    public Organization updateOrganization(String organizationId, UpdateOrganizationRequest request) {
        Organization organization = getOrganization(organizationId);

        if (request == null || request.name() == null || request.name().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one valid field is required");
        }

        organization.setName(normalizeName(request.name()));
        return organizationRepository.save(organization);
    }

    @Transactional(readOnly = true)
    public Optional<Organization> findOrganizationById(String organizationId) {
        return organizationRepository.findById(organizationId);
    }

    public String normalizeCode(String code) {
        if (code == null || code.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Organization code is required");
        }
        String normalized = code.trim().replace(' ', '-').toUpperCase(Locale.ROOT);
        if (!normalized.matches("^[A-Z0-9][A-Z0-9_-]*$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Organization code is invalid");
        }
        return normalized;
    }

    public String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Organization name is required");
        }
        return name.trim();
    }
}
