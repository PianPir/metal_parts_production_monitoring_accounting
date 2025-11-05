package com.metal_parts_production_monitoring_accounting.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "work_order")
@Data
public class WorkOrder {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_batch_id")
    private MaterialBatch materialBatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id")
    private Machine machine;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @Column(nullable = false)
    private LocalDateTime plannedStart;

    @Column(nullable = false)
    private LocalDateTime plannedEnd;

    @Column
    private LocalDateTime actualStart;

    @Column
    private LocalDateTime actualEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkOrderStatus status;

}
