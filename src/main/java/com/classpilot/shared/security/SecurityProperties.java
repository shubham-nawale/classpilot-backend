package com.classpilot.shared.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "classpilot.security")
public record SecurityProperties(String adminUsername, String adminPassword) {

    public void validate() {
        if (adminUsername == null || adminUsername.isBlank()) {
            throw new IllegalStateException("classpilot.security.admin-username must be configured.");
        }
        if (adminPassword == null || adminPassword.isBlank()) {
            throw new IllegalStateException("classpilot.security.admin-password must be configured.");
        }
    }
}
