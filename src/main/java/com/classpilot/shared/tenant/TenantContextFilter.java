package com.classpilot.shared.tenant;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TenantContextFilter extends OncePerRequestFilter {

    public static final String HEADER_NAME = "X-Tenant-Id";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String tenantId = Optional.ofNullable(request.getHeader(HEADER_NAME))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .orElse(TenantContext.DEFAULT_TENANT_ID);

        TenantContext.setCurrentTenantId(tenantId);
        response.setHeader(HEADER_NAME, tenantId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
