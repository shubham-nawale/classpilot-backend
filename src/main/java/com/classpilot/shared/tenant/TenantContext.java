package com.classpilot.shared.tenant;

public final class TenantContext {

    public static final String DEFAULT_TENANT_ID = "default";

    private static final ThreadLocal<String> CURRENT_TENANT_ID = new ThreadLocal<>();

    private TenantContext() {
    }

    public static String getCurrentTenantId() {
        String tenantId = CURRENT_TENANT_ID.get();
        return tenantId == null || tenantId.isBlank() ? DEFAULT_TENANT_ID : tenantId.trim();
    }

    public static void setCurrentTenantId(String tenantId) {
        CURRENT_TENANT_ID.set(tenantId == null || tenantId.isBlank() ? DEFAULT_TENANT_ID : tenantId.trim());
    }

    public static void clear() {
        CURRENT_TENANT_ID.remove();
    }
}
