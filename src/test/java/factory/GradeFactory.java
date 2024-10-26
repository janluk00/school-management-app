package factory;

import com.janluk.schoolmanagementapp.common.model.GradeEntity;

import java.math.BigDecimal;

public class GradeFactory {

    public static GradeEntity aGrade(double grade, String schoolSubject) {
        return GradeEntity.builder()
                .grade(BigDecimal.valueOf(grade))
                .subjectName(schoolSubject)
                .build();
    }
}
