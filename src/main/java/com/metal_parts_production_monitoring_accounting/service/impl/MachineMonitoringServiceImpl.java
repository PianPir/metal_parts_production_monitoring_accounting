package com.metal_parts_production_monitoring_accounting.service.impl;

import com.metal_parts_production_monitoring_accounting.mapper.MachineStatusLogMapper;
import com.metal_parts_production_monitoring_accounting.model.Machine;
import com.metal_parts_production_monitoring_accounting.model.MachineStatus;
import com.metal_parts_production_monitoring_accounting.model.MachineStatusLog;
import com.metal_parts_production_monitoring_accounting.model.ProcessedPart;
import com.metal_parts_production_monitoring_accounting.payload.request.MachineMonitoringRequest;
import com.metal_parts_production_monitoring_accounting.payload.response.MachineMonitoringResponse;
import com.metal_parts_production_monitoring_accounting.payload.response.OeeReportDto;
import com.metal_parts_production_monitoring_accounting.repository.MachineRepository;
import com.metal_parts_production_monitoring_accounting.repository.MachineStatusLogRepository;
import com.metal_parts_production_monitoring_accounting.repository.ProcessedPartRepository;
import com.metal_parts_production_monitoring_accounting.service.MachineMonitoringService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MachineMonitoringServiceImpl implements MachineMonitoringService {

    private final MachineRepository machineRepository;
    private final MachineStatusLogRepository statusLogRepository;
    private final MachineStatusLogMapper machineStatusLogMapper;
    private final ProcessedPartRepository processedPartRepository;


    @Override
    @Transactional(readOnly = false)
    public void receiveMachineMonitoring(Long machineId, MachineMonitoringRequest request) {
        // вызов проходит через Spring-прокси → @Async сработает
        processMachineMonitoringInternal(machineId, request);
    }

    @Async
    @Transactional(readOnly = false)
    public void processMachineMonitoringInternal(Long machineId, MachineMonitoringRequest request) {
        Machine machine = machineRepository.findById(machineId)
                .orElseThrow(() -> new EntityNotFoundException("Machine not found"));

        MachineStatusLog machineStatusLog = new MachineStatusLog();
        machineStatusLog.setMachine(machine);
        machineStatusLog.setStatus(request.status());
        machineStatusLog.setTimestamp(request.timestamp());
        machineStatusLog.setVibrationLevel(request.vibrationLevel());
        machineStatusLog.setTemperatureCelsius(request.temperatureCelsius());

        statusLogRepository.save(machineStatusLog);

        if (machineStatusLog.getStatus() == MachineStatus.ERROR) {
            log.warn("Machine reporting ERROR status - Name: {}, Id: {}", machine.getName(), machine.getId());
        }
    }

    @Override
    public MachineMonitoringResponse getMachineMonitoringStatus(Long machineId) {
        MachineStatusLog machineStatusLog = statusLogRepository.findTopByMachineIdOrderByTimestampDesc(machineId)
                .orElseThrow(() -> new EntityNotFoundException("MachineLog not found"));
        return machineStatusLogMapper.toMachineStatusLogResponse(machineStatusLog);
    }

    @Override
    public OeeReportDto calculateOee(Long machineId, LocalDateTime from, LocalDateTime to) {
        Instant fromInstant = from.toInstant(ZoneOffset.UTC);
        Instant toInstant = to.toInstant(ZoneOffset.UTC);



        List<MachineStatusLog> logs = statusLogRepository
                .findByMachineIdAndTimestampBetweenOrderByTimestampAsc(machineId, fromInstant, toInstant);

        double availability = calculateAvailability(logs, fromInstant, toInstant);

        List<ProcessedPart> parts = processedPartRepository
                .findByMachineIdAndCreatedAtBetween(machineId, fromInstant, toInstant);

        double quality = calculateQuality(parts);

        double oee = availability * quality;

        return new OeeReportDto(availability, quality, oee);
    }

    private double calculateAvailability (List<MachineStatusLog> logs, Instant from, Instant to){
        if (logs.isEmpty()) {
            return 0.0;
        }

        List<MachineStatusLog> machineStatusLogs = logs.stream()
                .sorted(Comparator.comparing(MachineStatusLog::getTimestamp))
                .toList();

        Duration totalRunningDuration = Duration.ZERO;
        Instant currentStart = null;

        for (MachineStatusLog machineStatusLog : machineStatusLogs) {
            Instant timestamp = machineStatusLog.getTimestamp();

            // лог до периода
            if (timestamp.isBefore(from)) {
                if (machineStatusLog.getStatus() == MachineStatus.RUNNING) {
                    currentStart = from;
                }
                continue;
            }
            // лог после периода
            if (timestamp.isAfter(to)) {
                break;
            }

            // дог внутри периода
            if(machineStatusLog.getStatus()==MachineStatus.RUNNING){
                if(currentStart == null){
                    currentStart = timestamp;
                }
            } else if (currentStart!=null) {
                totalRunningDuration = totalRunningDuration.plus(Duration.between(currentStart, timestamp));
                currentStart = null;
            }
        }

        // если после последнего лога статус остался RUNNING
        if (currentStart != null) {
            totalRunningDuration = totalRunningDuration.plus(Duration.between(currentStart,to));
        }

        Duration totalPlannedDuration = Duration.between(from, to);

        if (totalRunningDuration.isZero()) {
            return 0.0;
        }

        return totalRunningDuration.toNanos() / (double) totalPlannedDuration.toNanos();
    }

    private double calculateQuality(List<ProcessedPart> parts){
        if (parts.isEmpty()) {
            return 1.0;
        }

        long usableParts = parts.stream()
                .filter(part -> part.getDefectReason() == null || part.getDefectReason().isBlank())
                .count();

        return usableParts / (double) parts.size();
    }

}