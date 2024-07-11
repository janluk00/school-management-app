package com.janluk.schoolmanagementapp.teacher.schema;

import java.math.BigDecimal;

public record StudentPerformanceReportDTO(
        String schoolClass,
        String schoolSubject,
        BigDecimal gradePointAverage
) {
    public StudentPerformanceReportDTO(String schoolClass, String schoolSubject, Double gradePointAverage) {
        this(
                schoolClass,
                schoolSubject,
                gradePointAverage != null ? BigDecimal.valueOf(gradePointAverage) : null
        );
    }
}
