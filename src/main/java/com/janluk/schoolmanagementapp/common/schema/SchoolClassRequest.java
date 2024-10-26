package com.janluk.schoolmanagementapp.common.schema;

import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import jakarta.validation.constraints.NotNull;

public record SchoolClassRequest(
        @NotNull(message = "School class type cannot be null!")
        ClassType classType
) {
}
