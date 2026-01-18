package com.metal_parts_production_monitoring_accounting.exception;

public class WorkOrderNotFoundException extends RuntimeException {
    public WorkOrderNotFoundException(String message) {
        super(message);
    }

}
