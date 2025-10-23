package com.metal_parts_production_monitoring_accounting.model;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "material_batches")
@Data
public class MaterialBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String alloyType;

    @Column(nullable = false, scale = 4, precision = 19)
    private BigDecimal weightKg;

    @Column(nullable = false)
    private String supplier;

    @Column(nullable = false)
    private LocalDateTime arrivalDate =  LocalDateTime.now();

}
