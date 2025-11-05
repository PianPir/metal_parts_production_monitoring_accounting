package com.metal_parts_production_monitoring_accounting.service;


import com.metal_parts_production_monitoring_accounting.payload.request.WorkOrderRequest;
import com.metal_parts_production_monitoring_accounting.payload.response.WorkOrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface WorkOrderService {
    WorkOrderResponse createWorkOrder(WorkOrderRequest request);
    WorkOrderResponse startWorkOrder(Long id);
    WorkOrderResponse completeWorkOrder(Long id, String partNumber, BigDecimal weightAfterProcessingKg, String defectReason);
    Page<WorkOrderResponse> getAllWorkOrders(Pageable pageable);
    WorkOrderResponse getWorkOrderById(Long id);
}
