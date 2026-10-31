package com.classpilot.framework.api;

import java.time.Instant;
import java.util.List;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        Instant timestamp,
        String correlationId,
        List<String> errors) {

    public static <T> ApiResponse<T> success(T data, String message, String correlationId) {
        return new ApiResponse<>(true, message, data, Instant.now(), correlationId, List.of());
    }

    public static <T> ApiResponse<T> failure(String message, String correlationId, List<String> errors) {
        return new ApiResponse<>(false, message, null, Instant.now(), correlationId, errors);
    }
}
