package com.metal_parts_production_monitoring_accounting.repository;


import com.metal_parts_production_monitoring_accounting.model.MachineStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MachineStatusLogRepository extends JpaRepository<MachineStatusLog, Long> {
    Optional<MachineStatusLog> findTopByMachineIdOrderByTimestampDesc(Long machineId);
}
