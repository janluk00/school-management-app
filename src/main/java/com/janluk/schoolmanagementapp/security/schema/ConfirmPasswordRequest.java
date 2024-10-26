package com.janluk.schoolmanagementapp.security.schema;

import com.janluk.schoolmanagementapp.common.annotation.PasswordsMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordsMatch
public record ConfirmPasswordRequest(
        @NotBlank(message = "Password cannot be blank!")
        @Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters!")
        String password,

        @NotBlank(message = "Confirm password cannot be blank!")
        @Size(min = 8, max = 32, message = "Confirm password must be between 8 and 32 characters!")
        String confirmPassword
) {
}
