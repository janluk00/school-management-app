package com.janluk.schoolmanagementapp.common.schema;

import lombok.Builder;

@Builder
public record TaughtSubjectDTO(
        String subjectName,
        String teacherName,
        String teacherSurname
) {
}
