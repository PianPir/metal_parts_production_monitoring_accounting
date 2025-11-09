package com.metal_parts_production_monitoring_accounting.service.impl;


import com.metal_parts_production_monitoring_accounting.model.Machine;
import com.metal_parts_production_monitoring_accounting.model.MachineStatus;
import com.metal_parts_production_monitoring_accounting.model.MachineStatusLog;
import com.metal_parts_production_monitoring_accounting.payload.request.MachineMonitoringRequest;
import com.metal_parts_production_monitoring_accounting.repository.MachineRepository;
import com.metal_parts_production_monitoring_accounting.repository.MachineStatusLogRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MachineMonitoringServiceImpl {
    private final MachineRepository machineRepository;
    private final MachineStatusLogRepository statusLogRepository;

    @Async
    @Transactional
    public void receiveMachineMonitoring(Long machineId, MachineMonitoringRequest request){
        Machine machine = machineRepository.findById(machineId).orElseThrow(
                () -> new EntityNotFoundException("Machine not found"));

        MachineStatusLog machineStatusLog = new MachineStatusLog();
        machineStatusLog.setMachine(machine);
        machineStatusLog.setStatus(request.status());
        machineStatusLog.setTimestamp(request.timestamp());
        machineStatusLog.setVibrationLevel(request.vibrationLevel());
        machineStatusLog.setTemperatureCelsius(request.temperatureCelsius());

        statusLogRepository.save(machineStatusLog);

        if(machineStatusLog.getStatus() == MachineStatus.ERROR){
            log.warn("Machine reporting ERROR status ","Name: ",machine.getName(),"Id: ",machine.getId());
        }

    }
    

}
