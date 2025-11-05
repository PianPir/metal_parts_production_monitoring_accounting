package com.metal_parts_production_monitoring_accounting.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "machine_status_log")
@Data
public class MachineStatusLog {

    @OneToOne
    @JoinColumn(name = "machine_id")
    private Machine machine;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = true)
    private Double temperatureCelsius;

    @Column(nullable = true)
    private Double vibrationLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MachineStatus status;
}
