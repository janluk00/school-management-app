package factory;

import com.janluk.schoolmanagementapp.common.model.SchoolClassEntity;
import com.janluk.schoolmanagementapp.common.model.SchoolSubjectEntity;
import com.janluk.schoolmanagementapp.common.model.TeacherEntity;
import com.janluk.schoolmanagementapp.common.model.CourseEntity;
import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.teacher.schema.CreateTeacherRequest;

import java.util.Collections;
import java.util.HashSet;

public class TeacherFactory {

    public static TeacherEntity aTeacherWithTutorOf(ClassType tutorClassType) {
        SchoolClassEntity tutorClass = SchoolClassEntity.builder()
                .name(tutorClassType.name())
                .build();

        return TeacherEntity.builder()
                .tutorClass(tutorClass)
                .build();
    }

    public static TeacherEntity aTeacherWithoutTutor() {
        return new TeacherEntity();
    }

    public static TeacherEntity aTeacherWithSubjectOf(SubjectType subjectType) {
        SchoolSubjectEntity subject = SchoolSubjectEntity.builder()
                .name(subjectType.name())
                .build();

        return TeacherEntity.builder()
                .taughtSubjects(new HashSet<>(Collections.singleton(subject)))
                .build();
    }

    public static TeacherEntity aTeacherWithoutSubject() {
        return TeacherEntity.builder()
                .taughtSubjects(new HashSet<>())
                .build();
    }

    public static TeacherEntity aTeacherWithUser(String email) {
        return TeacherEntity.builder()
                .user(UserFactory.aTeacherUser(email))
                .build();
    }

    public static TeacherEntity aTeacherWithoutCourse(String email) {
        return TeacherEntity.builder()
                .user(UserFactory.aTeacherUser(email))
                .courses(new HashSet<>())
                .build();
    }

    public static TeacherEntity aTeacherWithCourse(String email) {
        return TeacherEntity.builder()
                .user(UserFactory.aTeacherUser(email))
                .courses(new HashSet<>(
                        Collections.singleton(CourseFactory.aMathCourseForA1SchoolClass())
                ))
                .taughtSubjects(new HashSet<>(Collections.singleton(SchoolSubjectFactory.aSchoolSubjectMath())))
                .build();
    }

    public static TeacherEntity aTeacherWithCourse(String email, CourseEntity course) {
        return TeacherEntity.builder()
                .user(UserFactory.aTeacherUser(email))
                .courses(new HashSet<>(Collections.singleton(course)))
                .taughtSubjects(new HashSet<>(Collections.singleton(course.getSubject())))
                .build();
    }

    public static CreateTeacherRequest aCreateTeacherRequestWithEmail(String email) {
        return new CreateTeacherRequest(
                new HashSet<>(Collections.singleton(SubjectType.MATHEMATICS)),
                UserFactory.aCreateUserRequestWithEmail(email)
        );
    }
}
