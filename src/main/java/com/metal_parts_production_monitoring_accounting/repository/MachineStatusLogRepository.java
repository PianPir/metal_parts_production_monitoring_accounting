package com.metal_parts_production_monitoring_accounting.repository;


import com.metal_parts_production_monitoring_accounting.model.MachineStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


@Repository
public interface MachineStatusLogRepository extends JpaRepository<MachineStatusLog, Long> {
    Optional<MachineStatusLog> findTopByMachineIdOrderByTimestampDesc(Long machineId);
    // получить все логи станка за период. эквивалент:
    // SELECT *
    //FROM machine_status_log
    //WHERE machine_id = ? AND timestamp BETWEEN ? AND ?
    //ORDER BY timestamp ASC;
    List<MachineStatusLog> findByMachineIdAndTimestampBetweenOrderByTimestampAsc(
            Long machineId, Instant from, Instant to);
}
