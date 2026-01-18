package com.metal_parts_production_monitoring_accounting.exception;

import com.metal_parts_production_monitoring_accounting.model.WorkOrderStatus;

public class WorkOrderNotPendingException  extends RuntimeException {
    public WorkOrderNotPendingException(String message) {
        super(message);
    }

}
