package com.metal_parts_production_monitoring_accounting.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {

    @NotBlank
    @Size(min=3, max=20)
    private String username;

    @NotBlank
    @Size(min=3, max=40)
    private String password;
}
