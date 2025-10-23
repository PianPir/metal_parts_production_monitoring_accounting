package com.metal_parts_production_monitoring_accounting.controller;


import com.metal_parts_production_monitoring_accounting.model.MaterialBatch;
import com.metal_parts_production_monitoring_accounting.repository.MaterialBatchRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/batches")
public class MaterialBatchController {

    private final MaterialBatchRepository materialBatchRepository;

    public MaterialBatchController(MaterialBatchRepository materialBatchRepository) {
        this.materialBatchRepository = materialBatchRepository;
    }

    @PostMapping
    public ResponseEntity<MaterialBatch> createBatch(@Valid @RequestBody MaterialBatch materialBatch) {
        MaterialBatch batch = new MaterialBatch();
        batch.setAlloyType(materialBatch.getAlloyType());
        batch.setSupplier(materialBatch.getSupplier());
        batch.setWeightKg(materialBatch.getWeightKg());
        MaterialBatch savedBatch = materialBatchRepository.save(batch);
        return ResponseEntity.ok().body(savedBatch);
    }

    @GetMapping
    public ResponseEntity<?> getAllBatches() {
        return ResponseEntity.ok().body(materialBatchRepository.findAll());
    }

}
