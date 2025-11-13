package com.metal_parts_production_monitoring_accounting.service.impl;

import com.metal_parts_production_monitoring_accounting.mapper.MachineStatusLogMapper;
import com.metal_parts_production_monitoring_accounting.model.Machine;
import com.metal_parts_production_monitoring_accounting.model.MachineStatus;
import com.metal_parts_production_monitoring_accounting.model.MachineStatusLog;
import com.metal_parts_production_monitoring_accounting.payload.request.MachineMonitoringRequest;
import com.metal_parts_production_monitoring_accounting.payload.response.MachineMonitoringResponse;
import com.metal_parts_production_monitoring_accounting.payload.response.OeeReportDto;
import com.metal_parts_production_monitoring_accounting.repository.MachineRepository;
import com.metal_parts_production_monitoring_accounting.repository.MachineStatusLogRepository;
import com.metal_parts_production_monitoring_accounting.service.MachineMonitoringService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MachineMonitoringServiceImpl implements MachineMonitoringService {

    private final MachineRepository machineRepository;
    private final MachineStatusLogRepository statusLogRepository;
    private final MachineStatusLogMapper machineStatusLogMapper;



    @Override
    @Transactional(readOnly = false) // ← важно: транзакция на запись
    public void receiveMachineMonitoring(Long machineId, MachineMonitoringRequest request) {
        // Этот вызов проходит через Spring-прокси → @Async сработает
        processMachineMonitoringInternal(machineId, request);
    }

    @Override
    public MachineMonitoringResponse getMachineMonitoringStatus(Long machineId) {
        MachineStatusLog machineStatusLog = statusLogRepository.findTopByMachineIdOrderByTimestampDesc(machineId)
                .orElseThrow(() -> new EntityNotFoundException("MachineLog not found"));
        return machineStatusLogMapper.toMachineStatusLogResponse(machineStatusLog);
    }

    @Override
    public OeeReportDto calculateOee(Long machineId, LocalDateTime from, LocalDateTime to) {

        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Async
    @Transactional(readOnly = false) // ← транзакция на запись
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
}