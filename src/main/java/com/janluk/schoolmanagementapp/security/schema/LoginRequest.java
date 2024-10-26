package com.janluk.schoolmanagementapp.security.schema;

import jakarta.validation.constraints.*;

public record LoginRequest(
        @NotBlank(message = "Email cannot be blank!")
        @Email(message = "Invalid email!")
        String email,

        @NotBlank(message = "Password cannot be blank!")
        @Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters!")
        String password
) {
}
