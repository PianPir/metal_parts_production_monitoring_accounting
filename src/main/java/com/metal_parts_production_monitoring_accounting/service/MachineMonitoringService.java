package com.metal_parts_production_monitoring_accounting.service;


import com.metal_parts_production_monitoring_accounting.payload.request.MachineMonitoringRequest;
import com.metal_parts_production_monitoring_accounting.payload.response.MachineMonitoringResponse;
import com.metal_parts_production_monitoring_accounting.payload.response.OeeReportDto;

import java.time.LocalDateTime;

public interface MachineMonitoringService {
    OeeReportDto calculateOee(Long machineId, LocalDateTime from, LocalDateTime to);
    MachineMonitoringResponse getMachineMonitoringStatus(Long machineId);
    void receiveMachineMonitoring(Long machineId, MachineMonitoringRequest machineMonitoringRequest);
}
