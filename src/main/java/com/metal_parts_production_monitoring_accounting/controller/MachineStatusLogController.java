package com.metal_parts_production_monitoring_accounting.controller;

import com.metal_parts_production_monitoring_accounting.payload.request.MachineMonitoringRequest;
import com.metal_parts_production_monitoring_accounting.payload.response.MachineMonitoringResponse;
import com.metal_parts_production_monitoring_accounting.payload.response.OeeReportDto;
import com.metal_parts_production_monitoring_accounting.service.MachineMonitoringService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/machines/monitoring")
@RequiredArgsConstructor
public class MachineStatusLogController {

    private final MachineMonitoringService machineMonitoringService;

    @PostMapping("/{id}/status")
    public ResponseEntity<Void> receiveStatus(
            @PathVariable Long id,
            @RequestBody @Valid MachineMonitoringRequest request) {
        machineMonitoringService.receiveMachineMonitoring(id, request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}/current-status")
    public ResponseEntity<MachineMonitoringResponse> getCurrentStatus(@PathVariable Long id) {
        MachineMonitoringResponse response = machineMonitoringService.getMachineMonitoringStatus(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/oee")
    public ResponseEntity<OeeReportDto> getOee(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime to) {

        OeeReportDto oeeReport = machineMonitoringService.calculateOee(id, from, to);
        return ResponseEntity.ok(oeeReport);
    }
}