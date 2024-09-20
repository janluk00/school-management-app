package factory;

import com.janluk.schoolmanagementapp.common.model.SchoolClassEntity;
import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.schema.SchoolClassRequest;

import java.util.Collections;
import java.util.HashSet;

public class SchoolClassFactory {

    public static SchoolClassRequest aSchoolClassRequestA1() {
        return new SchoolClassRequest(ClassType.A1);
    }

    public static SchoolClassEntity aSchoolClassA1() {
        return SchoolClassEntity.builder()
                .name(ClassType.A1.name())
                .build();
    }

    public static SchoolClassEntity aSchoolClassA1WithCourse() {
        return SchoolClassEntity.builder()
                .name(ClassType.A1.name())
                .courses(new HashSet<>(Collections.singleton(CourseFactory.aMathCourseForA1SchoolClass())))
                .build();
    }
}
