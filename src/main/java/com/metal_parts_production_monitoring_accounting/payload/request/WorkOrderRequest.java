package com.metal_parts_production_monitoring_accounting.payload.request;

import com.metal_parts_production_monitoring_accounting.model.WorkOrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;


public record WorkOrderRequest (
    @NotNull Long materialBatchId,
    @NotNull Long machineId,
    @NotBlank String orderNumber,
    @NotNull LocalDateTime plannedStart,
    @NotNull LocalDateTime plannedEnd
)
{}
