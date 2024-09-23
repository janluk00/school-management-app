package factory;

import com.janluk.schoolmanagementapp.common.model.CourseEntity;
import com.janluk.schoolmanagementapp.common.model.SchoolClassEntity;
import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.course.schema.AssignTeacherToCourseRequest;
import com.janluk.schoolmanagementapp.course.schema.RemoveTeacherFromCourseRequest;

import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

public class CourseFactory {

    public static final String TEACHER_EMAIL = "teacher@gmail.com";

    public static CourseEntity aMathCourseForA1SchoolClass() {
        return CourseEntity.builder()
                .subject(SchoolSubjectFactory.aSchoolSubjectMath())
                .schoolClasses(new HashSet<>(Collections.singleton(SchoolClassFactory.aSchoolClassA1())))
                .teacher(TeacherFactory.aTeacherWithUser(TEACHER_EMAIL))
                .build();
    }

    public static CourseEntity aEnglishCourseForA1SchoolClass() {
        return CourseEntity.builder()
                .subject(SchoolSubjectFactory.aSchoolSubjectEnglish())
                .schoolClasses(new HashSet<>(Collections.singleton(SchoolClassFactory.aSchoolClassA1())))
                .teacher(TeacherFactory.aTeacherWithUser(TEACHER_EMAIL))
                .build();
    }

    public static CourseEntity aCourseForSchoolClass(SubjectType subjectType, SchoolClassEntity schoolClass) {
        return CourseEntity.builder()
                .subject(SchoolSubjectFactory.aSchoolSubject(subjectType))
                .schoolClasses(new HashSet<>(Collections.singleton(schoolClass)))
                .teacher(TeacherFactory.aTeacherWithUser(TEACHER_EMAIL))
                .build();
    }

    public static AssignTeacherToCourseRequest anAssignTeacherToCourseRequest(UUID teacherId) {
        return new AssignTeacherToCourseRequest(
                ClassType.A1,
                SubjectType.MATHEMATICS,
                teacherId
        );
    }

    public static RemoveTeacherFromCourseRequest aRemoveTeacherFromCourseRequest(UUID teacherId, ClassType classType) {
        return new RemoveTeacherFromCourseRequest(
                classType,
                SubjectType.MATHEMATICS,
                teacherId
        );
    }
}
