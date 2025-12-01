package com.metal_parts_production_monitoring_accounting.controller;


import com.metal_parts_production_monitoring_accounting.config.TestSecurityConfig;
import com.metal_parts_production_monitoring_accounting.model.MachineType;
import com.metal_parts_production_monitoring_accounting.repository.MachineRepository;
import com.metal_parts_production_monitoring_accounting.repository.MaterialBatchRepository;
import com.metal_parts_production_monitoring_accounting.repository.WorkOrderRepository;
import com.metal_parts_production_monitoring_accounting.security.JwtTestUtil;
import com.metal_parts_production_monitoring_accounting.util.TestDataHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@Import({TestSecurityConfig.class,TestDataHelper.class})
@AutoConfigureMockMvc
public class WorkOrderControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired private MockMvc mockMvc;
    @Autowired private TestDataHelper testDataHelper;

    @Autowired private MachineRepository machineRepository;
    @Autowired private MaterialBatchRepository materialBatchRepository;
    @Autowired private WorkOrderRepository workOrderRepository;

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

    @BeforeEach
    void setUp() {
        testDataHelper.createMachine("CNC-001", MachineType.MILLING);
        testDataHelper.createMaterialBatch("Steel-45", new BigDecimal("100.00"), "Supplier A");
    }

    @AfterEach
    void tearDown() {
        // Очистка после теста
        workOrderRepository.deleteAll();
        machineRepository.deleteAll();
        materialBatchRepository.deleteAll();
    }

    @Test
    public void shouldCreateWorkOrderWithAdminRole() throws Exception {
        String token = JwtTestUtil.generateToken("adminuser", List.of("ROLE_ADMIN"),secret,lifetime);

        String requestBody = """
        {
            "materialBatchId": 1,
            "machineId": 1,
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

    @Test
    public void shouldReturnAllWorkOrders() throws Exception {
        String token = JwtTestUtil.generateToken("averageuser", List.of("ROLE_USER"),secret,lifetime);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/work-orders")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}