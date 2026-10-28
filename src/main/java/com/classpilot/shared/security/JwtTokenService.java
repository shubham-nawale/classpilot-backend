package com.classpilot.shared.security;

import com.classpilot.shared.tenant.TenantContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

    private final JwtProperties jwtProperties;
    private Key signingKey;

    public JwtTokenService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(encodeBase64Secret(jwtProperties.secret()));
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.expirationMs());

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String userId = authentication.getName();
        String role = roles.stream().findFirst().orElse("ROLE_USER");
        String orgId = TenantContext.getCurrentTenantId();
        boolean associatedWithOtherOrganization = false;

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("userId", userId)
                .claim("role", role)
                .claim("roles", roles)
                .claim("orgId", orgId)
                .claim("associatedWithOtherOrganization", associatedWithOtherOrganization)
                .claim("authorities", roles)
                .setIssuer(jwtProperties.issuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .requireIssuer(jwtProperties.issuer())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public String extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", String.class);
    }

    public String extractRole(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }

    public String extractOrgId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("orgId", String.class);
    }

    public boolean isAssociatedWithOtherOrganization(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("associatedWithOtherOrganization", Boolean.class) != null
                && claims.get("associatedWithOtherOrganization", Boolean.class);
    }

    public List<String> extractAuthorities(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Object roles = claims.get("roles");
        if (roles instanceof List<?> list) {
            return list.stream().map(String::valueOf).toList();
        }

        Object authorities = claims.get("authorities");
        if (authorities instanceof List<?> list) {
            return list.stream().map(String::valueOf).toList();
        }

        return List.of();
    }

    private String encodeBase64Secret(String secret) {
        if (secret.matches("^[A-Za-z0-9+/=]+$")
                && !secret.getBytes(StandardCharsets.UTF_8).equals(secret.getBytes(StandardCharsets.ISO_8859_1))) {
            return secret;
        }
        return java.util.Base64.getEncoder().encodeToString(secret.getBytes(StandardCharsets.UTF_8));
    }
}
