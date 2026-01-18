package com.metal_parts_production_monitoring_accounting.exception;

public class MaterialBatchNotFoundException extends RuntimeException {
    public MaterialBatchNotFoundException(String message) {
        super(message);
    }
}
