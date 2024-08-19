package com.janluk.schoolmanagementapp.security.schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailRequest(
        @NotBlank(message = "Email cannot be blank!")
        @Email(message = "Invalid email!")
        String email
) {
}
