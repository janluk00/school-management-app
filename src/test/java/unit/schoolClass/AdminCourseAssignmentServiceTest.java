package unit.schoolClass;

import com.janluk.schoolmanagementapp.common.exception.TeacherNotTeachingSubjectException;
import com.janluk.schoolmanagementapp.common.model.SchoolClassEntity;
import com.janluk.schoolmanagementapp.common.model.SchoolSubjectEntity;
import com.janluk.schoolmanagementapp.common.model.TeacherEntity;
import com.janluk.schoolmanagementapp.common.model.CourseEntity;
import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.common.repository.port.SchoolSubjectRepository;
import com.janluk.schoolmanagementapp.schoolClass.exception.SchoolClassAlreadyHasTeacherOfSchoolSubjectException;
import com.janluk.schoolmanagementapp.schoolClass.exception.CourseNotAssignedToSchoolClassException;
import com.janluk.schoolmanagementapp.schoolClass.schema.AssignTeacherToCourseRequest;
import com.janluk.schoolmanagementapp.schoolClass.schema.RemoveTeacherFromCourseRequest;
import com.janluk.schoolmanagementapp.schoolClass.service.AdminCourseAssignmentService;
import factory.SchoolClassFactory;
import factory.CourseFactory;
import org.junit.jupiter.api.AfterEach;
import factory.TeacherFactory;
import org.junit.jupiter.api.Test;
import repository.InMemorySchoolClassRepository;
import repository.InMemorySchoolSubjectRepository;
import repository.InMemoryCourseRepository;
import repository.InMemoryTeacherRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdminCourseAssignmentServiceTest {

    private final InMemorySchoolClassRepository schoolClassRepository = new InMemorySchoolClassRepository();
    private final InMemoryTeacherRepository teacherRepository = new InMemoryTeacherRepository();
    private final InMemoryCourseRepository courseRepository = new InMemoryCourseRepository();
    private final SchoolSubjectRepository schoolSubjectRepository = new InMemorySchoolSubjectRepository();

    private final AdminCourseAssignmentService adminCourseAssignmentService = new AdminCourseAssignmentService(
            schoolClassRepository,
            teacherRepository,
            courseRepository,
            schoolSubjectRepository
    );

    private static final String TEACHER_EMAIL = "teacher@gmail.com";

    @AfterEach
    void clean() {
        teacherRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @Test
    void cannotAssignTeacherToCourseIfTeacherDoesNotTeachSubject() {
        // given
        TeacherEntity teacher = TeacherFactory.aTeacherWithoutSubject();
        UUID teacherId = teacher.getId();
        teacherRepository.save(teacher);

        AssignTeacherToCourseRequest request = CourseFactory.anAssignTeacherToCourseRequest(teacherId);

        // when
        TeacherNotTeachingSubjectException exception = assertThrows(
                TeacherNotTeachingSubjectException.class,
                () -> adminCourseAssignmentService.assignTeacherToCourse(request)
        );

        // then
        assertEquals(
                exception.getMessage(),
                "Teacher with id: %s does not teach subject: MATHEMATICS.".formatted(teacherId.toString())
        );
    }

    @Test
    void cannotAssignTeacherToCourseIfSchoolClassAlreadyHasTeacherForSubject() {
        // given
        SchoolClassEntity schoolClass = SchoolClassFactory.aSchoolClassA1WithCourse();
        CourseEntity course = schoolClass.getCourses().iterator().next();
        schoolClassRepository.updateSchoolClass(schoolClass);
        TeacherEntity teacher = TeacherFactory.aTeacherWithCourse(TEACHER_EMAIL, course);
        UUID teacherId = teacher.getId();

        teacherRepository.save(teacher);
        courseRepository.save(course);

        AssignTeacherToCourseRequest request = CourseFactory.anAssignTeacherToCourseRequest(
                teacherId
        );

        // when
        SchoolClassAlreadyHasTeacherOfSchoolSubjectException exception = assertThrows(
                SchoolClassAlreadyHasTeacherOfSchoolSubjectException.class,
                () -> adminCourseAssignmentService.assignTeacherToCourse(request)
        );

        // then
        assertEquals(
                exception.getMessage(),
                "School class with name: A1 already has a teacher of school subject with name: MATHEMATICS."
        );
    }

    @Test
    void canAssignTeacherToCourse() {
        // given
        SchoolClassEntity schoolClass = SchoolClassFactory.aSchoolClassA1WithCourse();
        CourseEntity course = schoolClass.getCourses().iterator().next();
        TeacherEntity teacher = TeacherFactory.aTeacherWithCourse(TEACHER_EMAIL, course);
        SchoolSubjectEntity schoolSubject = schoolSubjectRepository.getById(SubjectType.MATHEMATICS);
        UUID teacherId = teacher.getId();

        teacherRepository.save(teacher);

        AssignTeacherToCourseRequest request = CourseFactory.anAssignTeacherToCourseRequest(
                teacherId
        );

        // when
        String courseId = adminCourseAssignmentService.assignTeacherToCourse(request);

        // then
        CourseEntity addedCourse = courseRepository.getByTeacherAndSchoolSubject(
                teacherId,
                SubjectType.MATHEMATICS.name()
        ).orElse(null);

        assertThat(addedCourse).isNotNull();
        assertEquals(addedCourse.getTeacher().getId(), teacherId);
        assertThat(addedCourse.getSchoolClasses()).contains(schoolClass);
        assertEquals(addedCourse.getSubject(), schoolSubject);
    }

    @Test
    void cannotRemoveTeacherFromCourseIfNotAssignedToSchoolClass() {
        // given
        SchoolClassEntity schoolClass = SchoolClassFactory.aSchoolClassA1WithCourse();
        CourseEntity course = schoolClass.getCourses().iterator().next();
        TeacherEntity teacher = TeacherFactory.aTeacherWithCourse(TEACHER_EMAIL, course);
        UUID teacherId = teacher.getId();

        teacherRepository.save(teacher);

        RemoveTeacherFromCourseRequest request = CourseFactory.aRemoveTeacherFromCourseRequest(
                teacherId,
                ClassType.C3
        );

        // when
        CourseNotAssignedToSchoolClassException exception = assertThrows(
                CourseNotAssignedToSchoolClassException.class,
                () -> adminCourseAssignmentService.removeTeacherFromCourse(request)
        );

        // then
        assertEquals(
                exception.getMessage(),
                "School class: C3 does not participate in the course with id: %s".formatted(course.getId().toString())
        );
    }

    @Test
    void canRemoveTeacherFromCourse() {
        // given
        SchoolClassEntity schoolClass = SchoolClassFactory.aSchoolClassA1WithCourse();
        CourseEntity course = schoolClass.getCourses().iterator().next();
        TeacherEntity teacher = TeacherFactory.aTeacherWithCourse(TEACHER_EMAIL, course);
        UUID teacherId = teacher.getId();

        teacherRepository.save(teacher);
        courseRepository.save(course);

        RemoveTeacherFromCourseRequest request = CourseFactory.aRemoveTeacherFromCourseRequest(
                teacherId,
                ClassType.A1 // same school class as the teacher from the course
        );

        // when
        UUID courseId = UUID.fromString(adminCourseAssignmentService.removeTeacherFromCourse(request));

        // then
        CourseEntity addedCourse = courseRepository.getById(courseId);

        assertThat(addedCourse)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", courseId)
                .extracting(CourseEntity::getSchoolClasses)
                .satisfies(schoolClasses -> assertThat(schoolClasses).isEmpty());
    }
}