package com.janluk.schoolmanagementapp.teacher.schema;

import com.janluk.schoolmanagementapp.common.model.vo.GradeType;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import jakarta.validation.constraints.NotNull;

public record AddGradeRequest(
        @NotNull(message = "Grade cannot be null!")
        GradeType grade,

        @NotNull(message = "School subject type cannot be null!")
        SubjectType subjectType
) {
}
