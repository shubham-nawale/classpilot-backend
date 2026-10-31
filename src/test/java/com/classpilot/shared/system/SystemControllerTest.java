package com.classpilot.shared.system;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "classpilot.security.admin-username=test-admin",
        "classpilot.security.admin-password=test-pass"
})
class SystemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void currentUserRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/system/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void currentUserReturnsAuthenticatedIdentityAndTenant() throws Exception {
        mockMvc.perform(get("/api/v1/system/me")
                        .header("X-Tenant-Id", "tenant-42")
                        .with(httpBasic("test-admin", "test-pass")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("test-admin"))
                .andExpect(jsonPath("$.data.tenantId").value("tenant-42"))
                .andExpect(jsonPath("$.data.roles[0]").value("ROLE_ADMIN"));
    }
}
