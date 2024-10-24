package unit.teacher;

import com.janluk.schoolmanagementapp.common.mapper.GradeMapper;
import com.janluk.schoolmanagementapp.common.model.BaseEntity;
import com.janluk.schoolmanagementapp.common.model.GradeEntity;
import com.janluk.schoolmanagementapp.common.model.StudentEntity;
import com.janluk.schoolmanagementapp.common.model.TeacherEntity;
import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.model.vo.GradeType;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.common.repository.port.SchoolClassRepository;
import com.janluk.schoolmanagementapp.common.repository.port.SchoolSubjectRepository;
import com.janluk.schoolmanagementapp.common.schema.CourseDTO;
import com.janluk.schoolmanagementapp.student.mapper.StudentMapper;
import com.janluk.schoolmanagementapp.teacher.exception.TeacherNotInCourseException;
import com.janluk.schoolmanagementapp.teacher.schema.AddGradeRequest;
import com.janluk.schoolmanagementapp.teacher.schema.CreateGradeResponse;
import com.janluk.schoolmanagementapp.teacher.service.TeacherAccountService;
import factory.StudentFactory;
import factory.TeacherFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import repository.InMemorySchoolClassRepository;
import repository.InMemorySchoolSubjectRepository;
import repository.InMemoryStudentRepository;
import repository.InMemoryTeacherRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

class TeacherAccountServiceTest {

    private final InMemoryTeacherRepository teacherRepository = new InMemoryTeacherRepository();
    private final InMemoryStudentRepository studentRepository = new InMemoryStudentRepository();
    private final SchoolClassRepository schoolClassRepository = new InMemorySchoolClassRepository();
    private final SchoolSubjectRepository schoolSubjectRepository = new InMemorySchoolSubjectRepository();
    private final GradeMapper gradeMapper = new GradeMapper();
    private final StudentMapper studentMapper = new StudentMapper();

    private final TeacherAccountService teacherAccountService = new TeacherAccountService(
            teacherRepository,
            studentRepository,
            schoolClassRepository,
            schoolSubjectRepository,
            gradeMapper,
            studentMapper
    );

    private static final String TEACHER_EMAIL = "teacher@gmail.com";

    @AfterEach
    void clean() {
        teacherRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    void shouldReturnCoursesForTeacherWhenCoursesExist() {
        // given
        TeacherEntity teacher = TeacherFactory.aTeacherWithCourse(TEACHER_EMAIL);
        String teacherEmail = teacher.getUser().getEmail();
        teacherRepository.save(teacher);

        // when
        List<CourseDTO> courseDTOS = teacherAccountService.getAllCoursesByTeacher(teacherEmail);

        // then
        assertThat(courseDTOS)
                .hasSize(1)
                .satisfies(courseDTOs -> {
                    CourseDTO course = courseDTOs.get(0);

                    assertEquals(course.className(), ClassType.A1.name());
                    assertEquals(course.schoolSubject(), SubjectType.MATHEMATICS.name());
                });
    }

    @Test
    void shouldThrowExceptionWhenAddingGradeAndTeacherHasNoCourses() {
        // given
        TeacherEntity teacher = TeacherFactory.aTeacherWithoutCourse(TEACHER_EMAIL);
        StudentEntity student = StudentFactory.aStudentInClass(ClassType.A1);
        UUID studentId = student.getId();
        teacherRepository.save(teacher);
        studentRepository.save(student);
        AddGradeRequest request = new AddGradeRequest(GradeType.B, SubjectType.MATHEMATICS);

        // when
        TeacherNotInCourseException teacherNotInCourseException = assertThrows(
                TeacherNotInCourseException.class,
                () -> teacherAccountService.addGrade(studentId, request, TEACHER_EMAIL)
        );

        // then
        assertEquals(
                teacherNotInCourseException.getMessage(),
                "Teacher with email %s is not assigned to course with school subject MATHEMATICS and school class A1."
                        .formatted(TEACHER_EMAIL)
        );
    }

    @Test
    void shouldThrowExceptionWhenAddingGradeToUnassignedCourse() {
        // given
        TeacherEntity teacher = TeacherFactory.aTeacherWithCourse(TEACHER_EMAIL);
        StudentEntity student = StudentFactory.aStudentInClass(ClassType.A1);
        UUID studentId = student.getId();
        teacherRepository.save(teacher);
        studentRepository.save(student);
        AddGradeRequest request = new AddGradeRequest(GradeType.B, SubjectType.BIOLOGY); // teacher doesn't have the BIOLOGY course

        // when
        TeacherNotInCourseException teacherNotInCourseException = assertThrows(
                TeacherNotInCourseException.class,
                () -> teacherAccountService.addGrade(studentId, request, TEACHER_EMAIL)
        );

        // then
        assertEquals(
                teacherNotInCourseException.getMessage(),
                "Teacher with email %s is not assigned to course with school subject BIOLOGY and school class A1."
                        .formatted(TEACHER_EMAIL)
        );
    }

    @Test
    void shouldAddGradeWhenTeacherIsAssignedToCourse() {
        // given
        TeacherEntity teacher = TeacherFactory.aTeacherWithCourse(TEACHER_EMAIL);
        StudentEntity student = StudentFactory.aStudentInClass(ClassType.A1);
        UUID studentId = student.getId();
        teacherRepository.save(teacher);
        studentRepository.save(student);
        AddGradeRequest request = new AddGradeRequest(GradeType.B, SubjectType.MATHEMATICS);

        // when
        CreateGradeResponse response = teacherAccountService.addGrade(studentId, request, TEACHER_EMAIL);
        String gradeId = response.gradeId();

        // then
        StudentEntity updatedStudent = studentRepository.getById(studentId);
        GradeEntity addedGrade = updatedStudent.getGrades().stream()
                .filter(g -> g.getId().equals(UUID.fromString(gradeId)))
                .findFirst()
                .orElse(null);

        assertThat(updatedStudent.getGrades())
                .isNotEmpty()
                .extracting(BaseEntity::getId)
                .containsOnly(UUID.fromString(gradeId));

        assertThat(addedGrade)
                .isNotNull()
                .hasFieldOrPropertyWithValue("subjectName", SubjectType.MATHEMATICS.name())
                .hasFieldOrPropertyWithValue("grade", BigDecimal.valueOf(5))
                .hasFieldOrPropertyWithValue("teacherId", teacher.getId());
    }
}
