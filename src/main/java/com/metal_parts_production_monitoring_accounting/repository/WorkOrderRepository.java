package com.metal_parts_production_monitoring_accounting.repository;

import com.metal_parts_production_monitoring_accounting.model.WorkOrder;
import com.metal_parts_production_monitoring_accounting.model.WorkOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {

    // подгружает machine и materialBatch сразу при поиске по ID
    @EntityGraph(attributePaths = {"machine", "materialBatch"})
    Optional<WorkOrder> findById(Long id);

    // связи при пагинации
    @EntityGraph(attributePaths = {"machine", "materialBatch"})
    Page<WorkOrder> findAll(Pageable pageable);

    // поиск по статусу
    @EntityGraph(attributePaths = {"machine", "materialBatch"})
    Page<WorkOrder> findByStatus(WorkOrderStatus status, Pageable pageable);
}
