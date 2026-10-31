package com.classpilot.organization.domain.repository;

import com.classpilot.organization.domain.model.Organization;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, String> {

    Optional<Organization> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);
}
