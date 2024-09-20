package factory;

import com.janluk.schoolmanagementapp.common.model.SchoolSubjectEntity;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.common.schema.SchoolSubjectRequest;

import java.util.Collections;
import java.util.HashSet;

public class SchoolSubjectFactory {

    public static SchoolSubjectRequest aSchoolSubjectRequestMath() {
        return new SchoolSubjectRequest(SubjectType.MATHEMATICS);
    }

    public static SchoolSubjectEntity aSchoolSubjectMath() {
        return SchoolSubjectEntity.builder()
                .name(SubjectType.MATHEMATICS.name())
                .build();
    }

    public static SchoolSubjectEntity aSchoolSubjectEnglish() {
        return SchoolSubjectEntity.builder()
                .name(SubjectType.ENGLISH.name())
                .build();
    }

    public static SchoolSubjectEntity aSchoolSubjectWithMathCourse() {
        return SchoolSubjectEntity.builder()
                .name(SubjectType.MATHEMATICS.name())
                .courses(new HashSet<>(Collections.singleton(CourseFactory.aMathCourseForA1SchoolClass())))
                .build();
    }

    public static SchoolSubjectEntity aSchoolSubjectWithEnglishCourse() {
        return SchoolSubjectEntity.builder()
                .name(SubjectType.ENGLISH.name())
                .courses(new HashSet<>(Collections.singleton(CourseFactory.aEnglishCourseForA1SchoolClass())))
                .build();
    }
}
