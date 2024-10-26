package com.janluk.schoolmanagementapp.student.schema;

import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.schema.CreateUserRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreateStudentRequest(
        @NotNull(message = "School class cannot be null!")
        ClassType schoolClass,

        @Valid
        @NotNull(message = "User cannot be null!")
        CreateUserRequest user
) {
}
