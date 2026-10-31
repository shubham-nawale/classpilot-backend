package com.classpilot.framework.security;

public record CurrentUser(String username, String tenantId, String roles) {
}
