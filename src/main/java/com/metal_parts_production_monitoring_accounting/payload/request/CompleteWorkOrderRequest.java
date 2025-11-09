package com.metal_parts_production_monitoring_accounting.payload.request;

import java.math.BigDecimal;

public record CompleteWorkOrderRequest(
        String partNumber,
        BigDecimal weightAfterProcessingKg,
        String defectReason) {

}
