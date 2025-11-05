package com.metal_parts_production_monitoring_accounting.payload.response;


import com.metal_parts_production_monitoring_accounting.model.WorkOrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record WorkOrderResponse (


        @NotNull Long id,
        @NotBlank String orderNumber,
        @NotBlank String alloyType,
        @NotBlank String machineName,
        @NotNull LocalDateTime plannedStart,
        @NotNull LocalDateTime plannedEnd,
        @NotNull LocalDateTime actualStart,
        @NotNull LocalDateTime actualEnd,
        @NotNull WorkOrderStatus status

) {}
