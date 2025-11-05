package com.metal_parts_production_monitoring_accounting.payload.request;

import com.metal_parts_production_monitoring_accounting.model.MachineType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MachineRequest(
        @NotBlank String name,
        @NotNull MachineType type
) {}