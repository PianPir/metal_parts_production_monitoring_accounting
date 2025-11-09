package com.metal_parts_production_monitoring_accounting.payload.request;

import com.metal_parts_production_monitoring_accounting.model.MachineStatus;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record MachineMonitoringRequest(
        @NotNull Instant timestamp,
        @NotNull MachineStatus status,
        Double temperatureCelsius,
        Double vibrationLevel
) {
}
