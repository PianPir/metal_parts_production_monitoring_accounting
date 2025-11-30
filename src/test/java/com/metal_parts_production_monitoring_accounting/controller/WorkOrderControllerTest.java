package com.metal_parts_production_monitoring_accounting.controller;


import com.metal_parts_production_monitoring_accounting.config.TestSecurityConfig;
import com.metal_parts_production_monitoring_accounting.security.JwtTestUtil;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc
public class WorkOrderControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private MockMvc mockMvc;

    @Value("${testing.app.secret}")
    private String secret;

    @Value("${testing.app.lifetime}")
    private long lifetime;

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.username", postgres::getUsername);
    }

    @Test
    public void shouldCreateWorkOrderWithAdminRole() throws Exception {
        String token = JwtTestUtil.generateToken("adminuser", List.of("ROLE_ADMIN"),secret,lifetime);

        String requestBody = """
        {
            "materialBatchId": 1,
            "machineId": 2,
            "orderNumber": "ORDER-123",
            "plannedStart": "2025-12-01T10:00:00",
            "plannedEnd": "2025-12-01T12:00:00"
        }
        """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/work-orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderNumber").value("ORDER-123"));
    }

}