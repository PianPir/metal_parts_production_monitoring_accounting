package com.metal_parts_production_monitoring_accounting.service.impl;


import com.metal_parts_production_monitoring_accounting.mapper.WorkOrderMapper;
import com.metal_parts_production_monitoring_accounting.model.*;
import com.metal_parts_production_monitoring_accounting.payload.request.WorkOrderRequest;
import com.metal_parts_production_monitoring_accounting.payload.response.WorkOrderResponse;
import com.metal_parts_production_monitoring_accounting.repository.MachineRepository;
import com.metal_parts_production_monitoring_accounting.repository.MaterialBatchRepository;
import com.metal_parts_production_monitoring_accounting.repository.ProcessedPartRepository;
import com.metal_parts_production_monitoring_accounting.repository.WorkOrderRepository;
import com.metal_parts_production_monitoring_accounting.service.WorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkOrderServiceImpl implements WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final MaterialBatchRepository materialBatchRepository;
    private final MachineRepository machineRepository;
    private final ProcessedPartRepository processedPartRepository;
    private final WorkOrderMapper workOrderMapper;

    @Override
    @Transactional
    public WorkOrderResponse createWorkOrder(WorkOrderRequest request){
        if(request.plannedStart().isAfter(request.plannedEnd())){
            throw new IllegalArgumentException("Planned start must be before planned end");
        }

        MaterialBatch Batch = materialBatchRepository.findById(request.materialBatchId()).orElseThrow(
                () -> new IllegalArgumentException("Material batch not found"));

        Machine machine = machineRepository.findById(request.machineId()).orElseThrow(
                () -> new IllegalArgumentException("Machine not found"));

        WorkOrder workOrder = new WorkOrder();
        workOrder.setMaterialBatch(Batch);
        workOrder.setMachine(machine);
        workOrder.setOrderNumber(request.orderNumber());
        workOrder.setPlannedStart(request.plannedStart());
        workOrder.setPlannedEnd(request.plannedEnd());
        workOrder.setStatus(WorkOrderStatus.PENDING);

        WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);
        return workOrderMapper.toResponse(savedWorkOrder);
    }

    @Override
    @Transactional
    public WorkOrderResponse startWorkOrder(Long workOrderId){
        WorkOrder workOrder = workOrderRepository.findById(workOrderId).orElseThrow(
                () -> new IllegalArgumentException("Work order not found"));

        if (workOrder.getStatus() != WorkOrderStatus.PENDING) {
            throw new IllegalStateException("WorkOrder must be PENDING to start");
        }

        workOrder.setStatus(WorkOrderStatus.RUNNING);
        workOrder.setActualStart(LocalDateTime.now());
        WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);
        return workOrderMapper.toResponse(savedWorkOrder);
    }

    @Override
    @Transactional
    public WorkOrderResponse completeWorkOrder (Long workOrderId,
                                                String partNumber,
                                                BigDecimal weightAfterProcessingKg,
                                                String defectReason){

        WorkOrder workOrder = workOrderRepository.findById(workOrderId).orElseThrow(
                () -> new IllegalArgumentException("Work order not found"));

        if (workOrder.getStatus() != WorkOrderStatus.RUNNING) {
            throw new IllegalStateException("WorkOrder must be RUNNING to complete");
        }

        workOrder.setActualEnd(LocalDateTime.now());
        workOrder.setStatus(WorkOrderStatus.COMPLETED);

        ProcessedPart processedPart = new ProcessedPart();
        processedPart.setPartNumber(partNumber);
        processedPart.setWeightAfterProcessingKg(weightAfterProcessingKg);
        processedPart.setDefectReason(defectReason);
        processedPart.setCreatedAt(LocalDateTime.now());
        processedPart.setWorkOrder(workOrder);

        processedPartRepository.save(processedPart);
        WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);
        return workOrderMapper.toResponse(savedWorkOrder);
    }

    @Override
    public Page<WorkOrderResponse> getAllWorkOrders(Pageable pageable) {
        Page<WorkOrder> page = workOrderRepository.findAll(pageable);
        return page.map(workOrderMapper::toResponse);
    }

    @Override
    public WorkOrderResponse getWorkOrderById (Long workOrderId){
        WorkOrder workOrder = workOrderRepository.findById(workOrderId).orElseThrow(
                () -> new IllegalArgumentException("Work order not found"));
        return workOrderMapper.toResponse(workOrder);
    }
}