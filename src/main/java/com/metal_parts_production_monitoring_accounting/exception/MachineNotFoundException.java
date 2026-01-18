package com.metal_parts_production_monitoring_accounting.exception;

public class MachineNotFoundException extends RuntimeException {
    public MachineNotFoundException(String message) {
        super(message);
    }
}
