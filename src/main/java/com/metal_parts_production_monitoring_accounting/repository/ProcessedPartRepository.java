package com.metal_parts_production_monitoring_accounting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.metal_parts_production_monitoring_accounting.model.ProcessedPart;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessedPartRepository extends JpaRepository<ProcessedPart, Long> {
    Optional<ProcessedPart> findByPartNumber(String processedPartId);
}