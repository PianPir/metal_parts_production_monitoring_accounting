package com.metal_parts_production_monitoring_accounting.payload.response;

import com.metal_parts_production_monitoring_accounting.model.MachineStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record MachineMonitoringResponse(
        @NotBlank String machineName,
        @NotNull Instant timestamp,
        @NotNull MachineStatus status,
        @NotNull Double temperatureCelsius,
        @NotNull Double vibrationLevel
) {
}
