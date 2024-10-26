package com.janluk.schoolmanagementapp.course.schema;

import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RemoveTeacherFromCourseRequest(
        @NotNull(message = "School class type cannot be null!")
        ClassType classType,

        @NotNull(message = "School subject type cannot be null!")
        SubjectType subjectType,

        @NotNull(message = "Teacher ID cannot be null!")
        UUID teacherId
) {
}
