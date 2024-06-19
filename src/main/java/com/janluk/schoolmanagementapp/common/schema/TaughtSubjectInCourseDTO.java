package com.janluk.schoolmanagementapp.common.schema;

import lombok.Builder;

@Builder
public record TaughtSubjectInCourseDTO(
        String subjectName,
        String teacherName,
        String teacherSurname
) {
}
