package com.metal_parts_production_monitoring_accounting.controller;


import com.metal_parts_production_monitoring_accounting.model.Machine;
import com.metal_parts_production_monitoring_accounting.payload.request.MachineRequest;
import com.metal_parts_production_monitoring_accounting.repository.MachineRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/machines")
public class MachineController {

    private final MachineRepository machineRepository;

    public MachineController(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    @PostMapping
    public ResponseEntity<Machine> registerMachine(@Valid @RequestBody MachineRequest machineRequest) {
        Machine machine = new Machine();
        machine.setName(machineRequest.name());
        machine.setMachineType(machineRequest.type());
        Machine savedMachine = machineRepository.save(machine);
        return ResponseEntity.ok().body(savedMachine);
    }

    @GetMapping
    public ResponseEntity<?> getAllMachines() {
        return ResponseEntity.ok().body(machineRepository.findAll());
    }
}
