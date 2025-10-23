package com.metal_parts_production_monitoring_accounting.repository;

import com.metal_parts_production_monitoring_accounting.model.MaterialBatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialBatchRepository extends JpaRepository<MaterialBatch,Long> {
}
