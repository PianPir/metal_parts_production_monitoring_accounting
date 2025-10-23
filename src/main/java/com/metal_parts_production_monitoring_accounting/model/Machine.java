package com.metal_parts_production_monitoring_accounting.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "machines")
@Data
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MachineType machineType;

}
