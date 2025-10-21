package com.metal_parts_production_monitoring_accounting.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SigninRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
