package com.janluk.schoolmanagementapp.security.schema;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull @NotEmpty String email,
        @NotNull @NotEmpty String password
) {
}
