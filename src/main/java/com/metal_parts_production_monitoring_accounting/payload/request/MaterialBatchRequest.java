package com.metal_parts_production_monitoring_accounting.payload.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class MaterialBatchRequest {
    @NotBlank
    private String alloyType;

    @NotNull
    private BigDecimal weightKg;

    @NotBlank
    private String supplier;

}
