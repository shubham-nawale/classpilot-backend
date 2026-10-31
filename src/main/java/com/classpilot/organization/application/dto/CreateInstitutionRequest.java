package com.classpilot.organization.application.dto;

import com.classpilot.organization.domain.enums.InstitutionType;

public record CreateInstitutionRequest(String name, String code, InstitutionType institutionType) {
}
