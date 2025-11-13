package com.metal_parts_production_monitoring_accounting.model;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "processed_parts")
@Data
public class ProcessedPart {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id")
    private WorkOrder workOrder;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String partNumber;

    @Column(nullable = false)
    private BigDecimal weightAfterProcessingKg;

    @Column(nullable = true)
    private String defectReason;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}
