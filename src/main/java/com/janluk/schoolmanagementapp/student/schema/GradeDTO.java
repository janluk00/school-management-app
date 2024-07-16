package com.janluk.schoolmanagementapp.student.schema;

import java.math.BigDecimal;

public record GradeDTO(
        String schoolSubject,
        BigDecimal grade
) {
    public GradeDTO(String schoolSubject, Double grade) {
        this(
                schoolSubject,
                grade != null ? BigDecimal.valueOf(grade) : null
        );
    }
}
