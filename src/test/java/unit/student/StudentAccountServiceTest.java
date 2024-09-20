package unit.student;

import com.janluk.schoolmanagementapp.common.model.GradeEntity;
import com.janluk.schoolmanagementapp.common.model.SchoolSubjectEntity;
import com.janluk.schoolmanagementapp.common.model.StudentEntity;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.common.schema.TaughtSubjectInCourseDTO;
import com.janluk.schoolmanagementapp.student.mapper.StudentMapper;
import com.janluk.schoolmanagementapp.student.schema.SchoolSubjectGradePointAverageDTO;
import com.janluk.schoolmanagementapp.student.schema.SchoolSubjectGradesDTO;
import com.janluk.schoolmanagementapp.student.service.StudentAccountService;
import factory.GradeFactory;
import factory.SchoolSubjectFactory;
import factory.StudentFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryGradeRepository;
import repository.InMemorySchoolSubjectRepository;
import repository.InMemoryStudentRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentAccountServiceTest {

    private final InMemoryGradeRepository gradeRepository = new InMemoryGradeRepository();
    private final StudentMapper studentMapper = new StudentMapper();
    private final InMemorySchoolSubjectRepository schoolSubjectRepository = new InMemorySchoolSubjectRepository();
    private final InMemoryStudentRepository studentRepository = new InMemoryStudentRepository();

    private final StudentAccountService studentAccountService = new StudentAccountService(
            gradeRepository,
            studentMapper,
            schoolSubjectRepository,
            studentRepository
    );

    private static final String STUDENT_EMAIL = "student@gmail.com";

    @AfterEach
    void clean() {
        gradeRepository.deleteAll();
        studentRepository.deleteAll();
        schoolSubjectRepository.deleteAll();
    }

    @Test
    void shouldReturnGradesGroupedBySchoolSubjectForGivenStudentEmail() {
        // given
        GradeEntity grade5 = GradeFactory.aGrade(5, SubjectType.MATHEMATICS.name());
        GradeEntity grade3 = GradeFactory.aGrade(3, SubjectType.MATHEMATICS.name());
        Set<GradeEntity> studentGrades = Stream.of(grade3, grade5)
                .collect(Collectors.toSet());
        StudentEntity student = StudentFactory.aStudentWithUserAndGrade(STUDENT_EMAIL, studentGrades);
        gradeRepository.initializeWithStudents(Collections.singleton(student));

        // when
        List<SchoolSubjectGradesDTO> subjectGradesDTOS = studentAccountService
                .getGradesGroupedBySchoolSubject(STUDENT_EMAIL);

        // then
        assertThat(subjectGradesDTOS)
                .isNotEmpty()
                .hasSize(1)
                .satisfies(subjectGrades -> {
                    SchoolSubjectGradesDTO firstSubjectGrades = subjectGrades.get(0);

                    assertEquals(firstSubjectGrades.schoolSubject(), "MATHEMATICS");
                    assertThat(firstSubjectGrades.grades())
                            .hasSize(2)
                            .containsOnly(grade3.getGrade(), grade5.getGrade());
                });
    }

    @Test
    void shouldReturnGPAGroupedBySchoolSubjectForGivenStudentEmail() {
        // given
        GradeEntity grade5 = GradeFactory.aGrade(5, SubjectType.MATHEMATICS.name());
        GradeEntity grade3 = GradeFactory.aGrade(3, SubjectType.MATHEMATICS.name());
        Set<GradeEntity> studentGrades = Stream.of(grade3, grade5)
                .collect(Collectors.toSet());
        StudentEntity student = StudentFactory.aStudentWithUserAndGrade(STUDENT_EMAIL, studentGrades);
        gradeRepository.initializeWithStudents(Collections.singleton(student));

        // when
        List<SchoolSubjectGradePointAverageDTO> subjectGPA_DTOS = studentAccountService
                .getGradePointAveragesGroupedBySchoolSubject(STUDENT_EMAIL);

        // then
        assertThat(subjectGPA_DTOS)
                .isNotEmpty()
                .hasSize(1)
                .satisfies(subjectGPA -> {
                    SchoolSubjectGradePointAverageDTO firstSubjectGPA = subjectGPA.get(0);

                    assertEquals(firstSubjectGPA.schoolSubject(), "MATHEMATICS");
                    assertEquals(firstSubjectGPA.gradePointAverage(), BigDecimal.valueOf(4.0));
                });
    }

    @Test
    void shouldReturnStudentCourses() {
        // given
        StudentEntity studentA1 = StudentFactory.aStudentWithUser(STUDENT_EMAIL);
        SchoolSubjectEntity mathA1 = SchoolSubjectFactory.aSchoolSubjectWithMathCourse();
        SchoolSubjectEntity englishA1 = SchoolSubjectFactory.aSchoolSubjectWithEnglishCourse();
        Set<SchoolSubjectEntity> schoolSubjects = Stream.of(mathA1, englishA1)
                .collect(Collectors.toSet());
        studentRepository.save(studentA1);
        schoolSubjectRepository.updateSchoolSubjects(schoolSubjects);

        // when
        List<TaughtSubjectInCourseDTO> subjectInCourseDTOS = studentAccountService.getAllCoursesByStudent(STUDENT_EMAIL);

        // then
        assertThat(subjectInCourseDTOS)
                .isNotEmpty()
                .hasSize(2)
                .extracting(TaughtSubjectInCourseDTO::subjectName)
                .containsOnly(SubjectType.MATHEMATICS.name(), SubjectType.ENGLISH.name());
    }
}
