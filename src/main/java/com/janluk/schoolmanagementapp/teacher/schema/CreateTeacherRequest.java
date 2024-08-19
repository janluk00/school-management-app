package com.janluk.schoolmanagementapp.teacher.schema;

import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.common.schema.CreateUserRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CreateTeacherRequest(
        @NotNull(message = "Taught subjects cannot be null!")
        @NotEmpty(message = "Taught subjects cannot be empty!")
        Set<SubjectType> taughtSubjects,

        @Valid
        @NotNull(message = "User cannot be null!")
        CreateUserRequest user
) {
}
