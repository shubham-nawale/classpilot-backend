package com.classpilot.shared.identity;

import java.util.List;

public record CurrentUser(String username, String tenantId, List<String> roles) {
}
