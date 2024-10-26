package com.janluk.schoolmanagementapp.common.schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateUserRequest(
        @NotBlank(message = "Name cannot be blank!")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters!")
        String name,

        @NotBlank(message = "Surname cannot be blank!")
        @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters!")
        String surname,

        @NotBlank(message = "Email cannot be blank!")
        @Email(message = "Invalid email!")
        String email,

        @Past(message = "Birth date must be in the past!")
        LocalDate birthDate
) {
}
