package com.classpilot.shared.system;

import com.classpilot.shared.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SystemController {

    @GetMapping("/system/ping")
    public ResponseEntity<ApiResponse<String>> ping(
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId) {
        return ResponseEntity.ok(ApiResponse.success("pong", "System ping succeeded", correlationId));
    }
}
