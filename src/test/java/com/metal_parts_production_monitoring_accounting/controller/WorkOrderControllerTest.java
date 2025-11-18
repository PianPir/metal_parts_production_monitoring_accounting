package com.metal_parts_production_monitoring_accounting.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WorkOrderController.class)
public class WorkOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

}
