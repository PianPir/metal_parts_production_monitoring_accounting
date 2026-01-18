package com.metal_parts_production_monitoring_accounting.exception;


import com.metal_parts_production_monitoring_accounting.payload.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(WorkOrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(WorkOrderNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("WORK_ORDER_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MachineNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(MachineNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("MACHINE_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MaterialBatchNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(MaterialBatchNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("MATERIAL_BATCH_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(WorkOrderNotPendingException.class)
    public ResponseEntity<ErrorResponse> handleException(WorkOrderNotPendingException ex) {
        ErrorResponse error = new ErrorResponse("WORK_ORDER_NOT_PENDING", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(WorkOrderNotRunningException.class)
    public ResponseEntity<ErrorResponse> handleException(WorkOrderNotRunningException ex) {
        ErrorResponse error = new ErrorResponse("WORK_ORDER_NOT_RUNNING", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(InvalidWorkOrderRequestException.class)
    public ResponseEntity<ErrorResponse> handleException(InvalidWorkOrderRequestException ex) {
        ErrorResponse error = new ErrorResponse("INVALID_WORK_ORDER_REQUEST", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unexpected error", e);
        ErrorResponse error = new ErrorResponse("INTERNAL_ERROR", "Something went wrong");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
