package com.metal_parts_production_monitoring_accounting.payload.response;

public record OeeReportDto(
        double availability,
        double quality,
        double oee
) {
}
