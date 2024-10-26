package com.janluk.schoolmanagementapp.student.schema;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record SchoolSubjectGradesDTO(
        String schoolSubject,
        List<BigDecimal> grades
) {
}
