package unit.teacher;

import com.janluk.schoolmanagementapp.common.model.*;
import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.teacher.schema.StudentPerformanceReportDTO;
import com.janluk.schoolmanagementapp.teacher.service.TeacherStatisticsService;
import factory.*;
import org.junit.jupiter.api.Test;
import repository.InMemoryTeacherRepository;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class TeacherStatisticsServiceTest {

    private final InMemoryTeacherRepository teacherRepository = new InMemoryTeacherRepository();

    private final TeacherStatisticsService teacherStatisticsService = new TeacherStatisticsService(
            teacherRepository
    );

    private static final String STUDENT_EMAIL1 = "student1@gmail.com";
    private static final String STUDENT_EMAIL2 = "student2@gmail.com";
    private static final String STUDENT_EMAIL3 = "student3@gmail.com";
    private static final String TEACHER_EMAIL = "teacher@gmail.com";

    @Test
    void shouldReturnStudentsPerformanceReport() {
        // given
        StudentEntity student1 = StudentFactory.aStudentWithUserAndGrade(STUDENT_EMAIL1, student1Grades());
        StudentEntity student2 = StudentFactory.aStudentWithUserAndGrade(STUDENT_EMAIL2, student2Grades());
        StudentEntity student3 = StudentFactory.aStudentWithUserAndGrade(STUDENT_EMAIL3, student3Grades());
        SchoolClassEntity schoolClassA1 = SchoolClassFactory.aSchoolClassWithStudents(
                ClassType.A1,
                new HashSet<>(Arrays.asList(student1, student2, student3))
        );
        CourseEntity englishCourseA1 = CourseFactory.aCourseForSchoolClass(SubjectType.ENGLISH, schoolClassA1);
        CourseEntity mathCourseA1 = CourseFactory.aCourseForSchoolClass(SubjectType.MATHEMATICS, schoolClassA1);
        Set<CourseEntity> courses = new HashSet<>(Arrays.asList(englishCourseA1, mathCourseA1));
        TeacherEntity teacher = TeacherFactory.aTeacherWithCourses(
                TEACHER_EMAIL,
                courses,
                teacherTaughtSubjects()
        );

        teacherRepository.save(teacher);

        // when
        List<StudentPerformanceReportDTO> reportDTOS = teacherStatisticsService.getStudentsPerformanceReportByTeacher(
                TEACHER_EMAIL
        );

        // then
        assertThat(reportDTOS).hasSize(2);

        StudentPerformanceReportDTO mathReport = reportDTOS.stream()
                .filter(report -> report.schoolSubject().equals("MATHEMATICS"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected report for MATHEMATICS"));

        StudentPerformanceReportDTO englishReport = reportDTOS.stream()
                .filter(report -> report.schoolSubject().equals("ENGLISH"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected report for ENGLISH"));

        assertThat(mathReport.schoolClass()).isEqualTo("A1");
        assertThat(mathReport.schoolSubject()).isEqualTo("MATHEMATICS");
        assertThat(mathReport.gradePointAverage()).isEqualTo(BigDecimal.valueOf(1.5));

        assertThat(englishReport.schoolClass()).isEqualTo("A1");
        assertThat(englishReport.schoolSubject()).isEqualTo("ENGLISH");
        assertThat(englishReport.gradePointAverage()).isEqualTo(BigDecimal.valueOf(4.0));
    }

    private Set<GradeEntity> student1Grades() {
        GradeEntity mathGradeE = GradeFactory.aGrade(2, SubjectType.MATHEMATICS.name());
        GradeEntity englishGradeB = GradeFactory.aGrade(5, SubjectType.ENGLISH.name());

        return new HashSet<>(Arrays.asList(mathGradeE, englishGradeB));
    }

    private Set<GradeEntity> student2Grades() {
        GradeEntity mathGradeF = GradeFactory.aGrade(1, SubjectType.MATHEMATICS.name());
        GradeEntity englishGradeE = GradeFactory.aGrade(2, SubjectType.ENGLISH.name());

        return new HashSet<>(Arrays.asList(mathGradeF, englishGradeE));
    }

    private Set<GradeEntity> student3Grades() {
        GradeEntity englishGradeB = GradeFactory.aGrade(5, SubjectType.ENGLISH.name());

        return new HashSet<>(Collections.singleton(englishGradeB));
    }

    private Set<SchoolSubjectEntity> teacherTaughtSubjects() {
        SchoolSubjectEntity mathSubject = SchoolSubjectFactory.aSchoolSubjectMath();
        SchoolSubjectEntity englishSubject = SchoolSubjectFactory.aSchoolSubjectEnglish();

        return new HashSet<>(Arrays.asList(mathSubject, englishSubject));
    }
}
