package com.classpilot.organization.domain.repository;

import com.classpilot.organization.domain.model.Institution;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstitutionRepository extends JpaRepository<Institution, String> {

    Optional<Institution> findByOrganizationIdAndId(String organizationId, String institutionId);

    Page<Institution> findByOrganizationId(String organizationId, Pageable pageable);

    boolean existsByOrganizationIdAndCodeIgnoreCase(String organizationId, String code);

    boolean existsByCodeIgnoreCase(String code);
}
