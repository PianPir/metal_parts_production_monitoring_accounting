package com.metal_parts_production_monitoring_accounting.util;


import com.metal_parts_production_monitoring_accounting.model.*;
import com.metal_parts_production_monitoring_accounting.repository.MachineRepository;
import com.metal_parts_production_monitoring_accounting.repository.MaterialBatchRepository;
import com.metal_parts_production_monitoring_accounting.repository.WorkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TestConfiguration
public class TestDataHelper {
    @Autowired
    private MachineRepository machineRepository;
    @Autowired
    private MaterialBatchRepository materialBatchRepository;
    @Autowired
    private WorkOrderRepository workOrderRepository;

    public Machine createMachine(String name, MachineType machineType) {
        Machine machine = new Machine();
        machine.setName(name);
        machine.setMachineType(machineType);
        return machineRepository.save(machine);
    }

    public MaterialBatch createMaterialBatch(String alloy, BigDecimal weight, String supplier) {
        MaterialBatch batch = new MaterialBatch();
        batch.setAlloyType(alloy);
        batch.setWeightKg(weight);
        batch.setSupplier(supplier);
        return materialBatchRepository.save(batch);
    }

    public WorkOrder createWorkOrder(MaterialBatch materialBatch, Machine machine,
                                     String orderNumber,
                                     LocalDateTime plannedStart,
                                     LocalDateTime plannedEnd) {

        WorkOrder workOrder = new WorkOrder();
        workOrder.setMaterialBatch(materialBatch);
        workOrder.setMachine(machine);
        workOrder.setOrderNumber(orderNumber);
        workOrder.setPlannedStart(plannedStart);
        workOrder.setPlannedEnd(plannedEnd);
        workOrder.setStatus(WorkOrderStatus.PENDING);
        return workOrderRepository.save(workOrder);

    }

}
