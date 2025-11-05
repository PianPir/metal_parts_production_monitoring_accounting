package com.metal_parts_production_monitoring_accounting.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record MaterialBatchRequest(
        @NotBlank String alloyType,
        @NotNull BigDecimal weightKg,
        @NotBlank String supplier
) {}