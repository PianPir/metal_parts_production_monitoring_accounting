package com.metal_parts_production_monitoring_accounting.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank @Size(min = 3, max = 20) String username,
        @NotBlank @Size(min = 3, max = 40) String password
) {}