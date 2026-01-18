package com.metal_parts_production_monitoring_accounting.exception;

public class InvalidWorkOrderRequestException extends RuntimeException {
    public InvalidWorkOrderRequestException(String message) {
        super(message);
    }
}
