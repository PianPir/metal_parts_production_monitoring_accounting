package com.metal_parts_production_monitoring_accounting.controller;



import com.metal_parts_production_monitoring_accounting.payload.request.WorkOrderRequest;
import com.metal_parts_production_monitoring_accounting.payload.response.WorkOrderResponse;
import com.metal_parts_production_monitoring_accounting.service.WorkOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/work-orders")
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    // Только ADMIN может создавать заказы
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkOrderResponse> createWorkOrder(
            @Valid @RequestBody WorkOrderRequest request) {
        WorkOrderResponse response = workOrderService.createWorkOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // просматривать список
    @GetMapping
    public ResponseEntity<Page<WorkOrderResponse>> getAllWorkOrders(Pageable pageable) {
        Page<WorkOrderResponse> responses = workOrderService.getAllWorkOrders(pageable);
        return ResponseEntity.ok(responses);
    }

    // Запуск заказа
    @PutMapping("/{id}/start")
    public ResponseEntity<WorkOrderResponse> startWorkOrder(@PathVariable Long id) {
        WorkOrderResponse response = workOrderService.startWorkOrder(id);
        return ResponseEntity.ok(response);
    }

    // Завершение заказа
    @PutMapping("/{id}/complete")
    public ResponseEntity<WorkOrderResponse> completeWorkOrder(@PathVariable Long id) {
        WorkOrderResponse response = workOrderService.completeWorkOrder(id);
        return ResponseEntity.ok(response);
    }
}