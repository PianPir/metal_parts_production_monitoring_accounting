package com.metal_parts_production_monitoring_accounting.util;


import com.metal_parts_production_monitoring_accounting.model.Machine;
import com.metal_parts_production_monitoring_accounting.model.MachineType;
import com.metal_parts_production_monitoring_accounting.model.MaterialBatch;
import com.metal_parts_production_monitoring_accounting.repository.MachineRepository;
import com.metal_parts_production_monitoring_accounting.repository.MaterialBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;

import java.math.BigDecimal;

@TestConfiguration
public class TestDataHelper {
    @Autowired
    private MachineRepository machineRepository;
    @Autowired
    private MaterialBatchRepository materialBatchRepository;

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

}
