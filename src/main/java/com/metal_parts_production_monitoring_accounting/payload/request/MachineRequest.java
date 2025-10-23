package com.metal_parts_production_monitoring_accounting.payload.request;


import com.metal_parts_production_monitoring_accounting.model.MachineType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MachineRequest {
    @NotBlank
    private String name;

    @NotNull
    private MachineType type;
}
