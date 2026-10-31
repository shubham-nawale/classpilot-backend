package com.classpilot.shared.system;

import com.classpilot.shared.api.ApiResponse;
import com.classpilot.shared.identity.CurrentUser;
import com.classpilot.shared.identity.CurrentUserProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SystemController {

    private final CurrentUserProvider currentUserProvider;

    public SystemController(CurrentUserProvider currentUserProvider) {
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping("/system/ping")
    public ResponseEntity<ApiResponse<String>> ping(
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId) {
        return ResponseEntity.ok(ApiResponse.success("pong", "System ping succeeded", correlationId));
    }

    @GetMapping("/system/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CurrentUser>> me(
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId) {
        CurrentUser currentUser = currentUserProvider.getCurrentUser()
                .orElseThrow(() -> new IllegalStateException("No authenticated current user found."));

        return ResponseEntity.ok(ApiResponse.success(currentUser, "Current user resolved", correlationId));
    }
}
