package com.janluk.schoolmanagementapp.common.mapper;

import com.janluk.schoolmanagementapp.common.model.GradeEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Component
public class GradeMapper {

    public GradeEntity toGradeEntity(BigDecimal grade, UUID studentId, String schoolSubject, UUID teacherId) {
        return GradeEntity.builder()
                .grade(grade)
                .issuedDate(Instant.now())
                .studentId(studentId)
                .teacherId(teacherId)
                .subjectName(schoolSubject)
                .build();
    }
}
