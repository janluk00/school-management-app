package com.janluk.schoolmanagementapp.common.schema;

import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import jakarta.validation.constraints.NotNull;

public record SchoolSubjectRequest(
        @NotNull(message = "School subject type cannot be null!")
        SubjectType subjectType
) {
}
