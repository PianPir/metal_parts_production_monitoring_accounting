package com.metal_parts_production_monitoring_accounting.service.impl;


import com.metal_parts_production_monitoring_accounting.mapper.WorkOrderMapper;
import com.metal_parts_production_monitoring_accounting.model.ProcessedPart;
import com.metal_parts_production_monitoring_accounting.payload.request.WorkOrderRequest;
import com.metal_parts_production_monitoring_accounting.payload.response.WorkOrderResponse;
import com.metal_parts_production_monitoring_accounting.repository.MachineRepository;
import com.metal_parts_production_monitoring_accounting.repository.MaterialBatchRepository;
import com.metal_parts_production_monitoring_accounting.repository.ProcessedPartRepository;
import com.metal_parts_production_monitoring_accounting.repository.WorkOrderRepository;
import com.metal_parts_production_monitoring_accounting.service.WorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    }

}
