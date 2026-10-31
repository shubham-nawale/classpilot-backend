package com.classpilot.shared.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "classpilot.security.jwt")
public record JwtProperties(
        String secret,
        Long expirationMs,
        String issuer) {

    public JwtProperties {
        if (secret == null || secret.isBlank()) {
            secret = "classpilot-local-dev-secret-change-me";
        }
        if (expirationMs == null || expirationMs <= 0) {
            expirationMs = 3_600_000L;
        }
        if (issuer == null || issuer.isBlank()) {
            issuer = "classpilot-backend";
        }
    }
}
