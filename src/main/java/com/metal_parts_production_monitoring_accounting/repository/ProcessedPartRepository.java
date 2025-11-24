package com.metal_parts_production_monitoring_accounting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.metal_parts_production_monitoring_accounting.model.ProcessedPart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProcessedPartRepository extends JpaRepository<ProcessedPart, Long> {
    Optional<ProcessedPart> findByPartNumber(String processedPartId);
    // Получить все детали, произведённые на станке за период
    @Query(" SELECT p FROM ProcessedPart p JOIN p.workOrder w WHERE w.machine.id = :machineId AND p.createdAt BETWEEN :from AND :to")
    List<ProcessedPart> findByMachineIdAndCreatedAtBetween(
            @Param("machineId") Long machineId,
            @Param("from") Instant from,
            @Param("to") Instant to);
}