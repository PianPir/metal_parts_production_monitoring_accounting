package com.metal_parts_production_monitoring_accounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MetalPartsProductionMonitoringAccountingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetalPartsProductionMonitoringAccountingApplication.class, args);
    }

}
