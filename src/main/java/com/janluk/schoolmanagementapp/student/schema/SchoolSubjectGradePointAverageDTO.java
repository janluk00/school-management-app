package com.janluk.schoolmanagementapp.student.schema;

import java.math.BigDecimal;

public record SchoolSubjectGradePointAverageDTO(
        String schoolSubject,
        BigDecimal gradePointAverage
) {
    public SchoolSubjectGradePointAverageDTO(String schoolSubject, Double gradePointAverage) {
        this(
                schoolSubject,
                gradePointAverage != null ? BigDecimal.valueOf(gradePointAverage) : null
        );
    }
}
