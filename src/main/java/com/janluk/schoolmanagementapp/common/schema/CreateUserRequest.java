package com.janluk.schoolmanagementapp.common.schema;

import java.time.LocalDate;

// TODO: ADD VALIDATION
public record CreateUserRequest(
        String name,
        String surname,
        String email,
        LocalDate birthDate
) {
}
