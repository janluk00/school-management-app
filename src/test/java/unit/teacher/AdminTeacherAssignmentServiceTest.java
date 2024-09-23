package unit.teacher;

import com.janluk.schoolmanagementapp.common.exception.TeacherNotTeachingSubjectException;
import com.janluk.schoolmanagementapp.common.model.SchoolClassEntity;
import com.janluk.schoolmanagementapp.common.model.SchoolSubjectEntity;
import com.janluk.schoolmanagementapp.common.model.TeacherEntity;
import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.common.schema.SchoolClassRequest;
import com.janluk.schoolmanagementapp.common.schema.SchoolSubjectRequest;
import com.janluk.schoolmanagementapp.teacher.exception.TeacherAlreadyTeachingSubjectException;
import com.janluk.schoolmanagementapp.teacher.exception.TeacherIsAlreadyTutorException;
import com.janluk.schoolmanagementapp.teacher.exception.TeacherNotAssignedAsTutorException;
import com.janluk.schoolmanagementapp.teacher.service.AdminTeacherAssignmentService;
import factory.SchoolClassFactory;
import factory.SchoolSubjectFactory;
import factory.TeacherFactory;
import repository.InMemorySchoolClassRepository;
import repository.InMemorySchoolSubjectRepository;
import repository.InMemoryTeacherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

class AdminTeacherAssignmentServiceTest {

    private final InMemoryTeacherRepository teacherRepository = new InMemoryTeacherRepository();
    private final InMemorySchoolClassRepository schoolClassRepository = new InMemorySchoolClassRepository();
    private final InMemorySchoolSubjectRepository schoolSubjectRepository = new InMemorySchoolSubjectRepository();

    private final AdminTeacherAssignmentService adminTeacherAssignmentService = new AdminTeacherAssignmentService(
            teacherRepository,
            schoolClassRepository,
            schoolSubjectRepository
    );

    private static final ClassType SCHOOL_CLASS_A1 = ClassType.A1;

    @AfterEach
    void clean() {
        teacherRepository.deleteAll();
    }

    @Test
    void shouldThrowExceptionWhenReassigningSameTutorToTeacher() {
        // given
        TeacherEntity teacherTutor = TeacherFactory.aTeacherWithTutorOf(SCHOOL_CLASS_A1);
        SchoolClassRequest schoolClassA1 = SchoolClassFactory.aSchoolClassRequestA1();
        UUID teacherId = teacherTutor.getId();
        teacherRepository.save(teacherTutor);

        // when
        TeacherIsAlreadyTutorException teacherIsAlreadyTutorException = assertThrows(
                TeacherIsAlreadyTutorException.class,
                () -> adminTeacherAssignmentService.assignTutorToTeacher(teacherId, schoolClassA1)
        );

        // then
        assertEquals(
                "Teacher with id: %s is already the tutor of class: A1.".formatted(teacherId.toString()),
                teacherIsAlreadyTutorException.getMessage()
        );
    }

    @Test
    void shouldAssignTutorToTeacher() {
        // given
        TeacherEntity teacherWithoutTutor = TeacherFactory.aTeacherWithoutTutor();
        SchoolClassRequest schoolClassA1 = SchoolClassFactory.aSchoolClassRequestA1();
        UUID teacherId = teacherWithoutTutor.getId();
        teacherRepository.save(teacherWithoutTutor);

        // when
        UUID tutorTeacherId = UUID.fromString(
                adminTeacherAssignmentService.assignTutorToTeacher(teacherId, schoolClassA1)
        );

        // then
        TeacherEntity teacher = teacherRepository.getById(tutorTeacherId);
        SchoolClassEntity tutorClass = schoolClassRepository.getById(schoolClassA1.classType());
        assertThat(teacher)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", teacherId)
                .hasFieldOrPropertyWithValue("tutorClass", tutorClass);
    }

    @Test
    void shouldThrowExceptionWhenRemovingNonAssignedTutor() {
        // given
        TeacherEntity teacherWithoutTutor = TeacherFactory.aTeacherWithoutTutor();
        UUID teacherId = teacherWithoutTutor.getId();
        teacherRepository.save(teacherWithoutTutor);

        // when
        TeacherNotAssignedAsTutorException teacherNotAssignedAsTutorException = assertThrows(
                TeacherNotAssignedAsTutorException.class,
                () -> adminTeacherAssignmentService.removeTutorAssignment(teacherId)
        );

        // then
        assertEquals(
                teacherNotAssignedAsTutorException.getMessage(),
                "Teacher with id %s is not assigned as a tutor to any school class.".formatted(teacherId)
        );
    }

    @Test
    void shouldRemoveTutorAssignmentFromTeacher() {
        // given
        TeacherEntity teacherTutor = TeacherFactory.aTeacherWithTutorOf(SCHOOL_CLASS_A1);
        UUID teacherTutorId = teacherTutor.getId();
        teacherRepository.save(teacherTutor);

        // when
        UUID teacherWithoutTutorId = UUID.fromString(adminTeacherAssignmentService.removeTutorAssignment(teacherTutorId));

        // then
        TeacherEntity teacherWithoutTutor = teacherRepository.getById(teacherWithoutTutorId);
        assertThat(teacherWithoutTutor)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", teacherTutorId)
                .hasFieldOrPropertyWithValue("tutorClass", null);
    }

    @Test
    void shouldThrowExceptionWhenAssigningSameSubjectToTeacher() {
        // given
        TeacherEntity teacherWithSubject = TeacherFactory.aTeacherWithSubjectOf(SubjectType.MATHEMATICS);
        SchoolSubjectRequest mathSubjectRequest = SchoolSubjectFactory.aSchoolSubjectRequestMath();
        UUID teacherId = teacherWithSubject.getId();
        teacherRepository.save(teacherWithSubject);

        // when
        TeacherAlreadyTeachingSubjectException teacherAlreadyTeachingSubjectException = assertThrows(
                TeacherAlreadyTeachingSubjectException.class,
                () -> adminTeacherAssignmentService.assignSubjectToTutor(teacherId, mathSubjectRequest)
        );

        // then
        assertEquals(
                "Teacher with id: %s is already teaching subject: MATHEMATICS.".formatted(teacherId.toString()),
                teacherAlreadyTeachingSubjectException.getMessage()
        );
    }

    @Test
    void shouldAssignSubjectToTeacher() {
        // given
        TeacherEntity teacherWithoutSubject = TeacherFactory.aTeacherWithoutSubject();
        SchoolSubjectRequest mathSubjectRequest = SchoolSubjectFactory.aSchoolSubjectRequestMath();
        UUID teacherId = teacherWithoutSubject.getId();
        teacherRepository.save(teacherWithoutSubject);

        // when
        UUID teacherIdWithSubject = UUID.fromString(
                adminTeacherAssignmentService.assignSubjectToTutor(teacherId, mathSubjectRequest)
        );

        // then
        TeacherEntity teacher = teacherRepository.getById(teacherIdWithSubject);
        SchoolSubjectEntity mathSubject = schoolSubjectRepository.getById(mathSubjectRequest.subjectType());
        assertThat(teacher)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", teacherId)
                .extracting(TeacherEntity::getTaughtSubjects)
                .satisfies(schoolSubjects -> assertThat(schoolSubjects).containsExactly(mathSubject));
    }

    @Test
    void shouldThrowExceptionWhenRemovingUnassignedSubjectFromTeacher() {
        // given
        TeacherEntity teacherWithoutSubject = TeacherFactory.aTeacherWithoutSubject();
        UUID teacherId = teacherWithoutSubject.getId();
        teacherRepository.save(teacherWithoutSubject);

        // when
        TeacherNotTeachingSubjectException teacherNotTeachingSubjectException = assertThrows(
                TeacherNotTeachingSubjectException.class,
                () -> adminTeacherAssignmentService.removeSubjectFromTeacher(teacherId, SubjectType.MATHEMATICS)
        );

        // then
        assertEquals(
                "Teacher with id: %s does not teach subject: MATHEMATICS.".formatted(teacherId.toString()),
                teacherNotTeachingSubjectException.getMessage()
        );
    }

    @Test
    void shouldRemoveSubjectFromTeacher() {
        // given
        TeacherEntity teacherWithSubject = TeacherFactory.aTeacherWithSubjectOf(SubjectType.MATHEMATICS);
        UUID teacherIdWithSubject = teacherWithSubject.getId();
        teacherRepository.save(teacherWithSubject);

        // when
        UUID teacherIdWithoutSubject = UUID.fromString(
                adminTeacherAssignmentService.removeSubjectFromTeacher(teacherIdWithSubject, SubjectType.MATHEMATICS)
        );

        // then
        TeacherEntity teacherWithoutSubject = teacherRepository.getById(teacherIdWithoutSubject);
        assertThat(teacherWithoutSubject)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", teacherIdWithSubject)
                .extracting(TeacherEntity::getTaughtSubjects)
                .satisfies(schoolSubjects -> assertThat(schoolSubjects).isEmpty());
    }
}
