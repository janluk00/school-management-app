package unit.teacher;

import com.janluk.schoolmanagementapp.common.email.EmailService;
import com.janluk.schoolmanagementapp.common.exception.EmailAlreadyExistsException;
import com.janluk.schoolmanagementapp.common.exception.NoResultFoundException;
import com.janluk.schoolmanagementapp.common.model.TeacherEntity;
import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.common.schema.CourseDTO;
import com.janluk.schoolmanagementapp.common.user.RoleAdder;
import com.janluk.schoolmanagementapp.common.user.UserValidator;
import com.janluk.schoolmanagementapp.teacher.criteria.TeacherSearcher;
import com.janluk.schoolmanagementapp.teacher.mapper.TeacherMapper;
import com.janluk.schoolmanagementapp.teacher.schema.CreateTeacherRequest;
import com.janluk.schoolmanagementapp.teacher.schema.TeacherDTO;
import com.janluk.schoolmanagementapp.teacher.service.AdminTeacherService;
import factory.TeacherFactory;
import org.junit.jupiter.api.AfterEach;
import repository.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

class AdminTeacherServiceTest {

    private final InMemoryUserRepository userRepository = new InMemoryUserRepository();
    private final InMemoryRoleRepository roleRepository = new InMemoryRoleRepository();
    private final UserValidator userValidator = new UserValidator(userRepository);
    private final InMemoryTeacherRepository teacherRepository = new InMemoryTeacherRepository();
    private final TeacherMapper teacherMapper = new TeacherMapper();
    private final RoleAdder roleAdder = new RoleAdder(roleRepository);
    private final TeacherSearcher teacherSearcher = new TeacherSearcher(teacherRepository);
    private final EmailService emailService = new FakeTestEmailService();

    private final AdminTeacherService adminTeacherService = new AdminTeacherService(
            userValidator,
            teacherRepository,
            teacherMapper,
            roleAdder,
            teacherSearcher,
            emailService
    );

    private static final String TEACHER_EMAIL = "teacher@gmail.com";
    private static final UUID RANDOM_UUID = UUID.randomUUID();

    @AfterEach
    void clean() {
        userRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @Test
    void cannotGetTeacherById() {
        // given
        TeacherEntity teacher = TeacherFactory.aTeacherWithUser(TEACHER_EMAIL);
        teacherRepository.save(teacher);

        // when
        NoResultFoundException noResultFoundException = assertThrows(
                NoResultFoundException.class,
                () -> adminTeacherService.getTeacherById(RANDOM_UUID)
        );

        // then
        assertEquals(
                noResultFoundException.getMessage(),
                "Could not find teacher with id: %s".formatted(RANDOM_UUID.toString())
        );
    }

    @Test
    void canGetTeacherById() {
        // given
        TeacherEntity teacher = TeacherFactory.aTeacherWithUser(TEACHER_EMAIL);
        UUID teacherId = teacher.getId();
        teacherRepository.save(teacher);

        // when
        TeacherDTO teacherDTO = adminTeacherService.getTeacherById(teacherId);

        // then
        assertThat(teacherDTO)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", teacherId);
    }

    @Test
    void shouldReturnTeacherCourse() {
        // given
        TeacherEntity teacher = TeacherFactory.aTeacherWithCourse(TEACHER_EMAIL);
        UUID teacherId = teacher.getId();
        teacherRepository.save(teacher);

        // when
        List<CourseDTO> courseDTOS = adminTeacherService.getAllCoursesByTeacherId(teacherId);

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
    void cannotCreateTeacherWithExistingEmail() {
        // given
        TeacherEntity teacher = TeacherFactory.aTeacherWithUser(TEACHER_EMAIL);
        teacherRepository.save(teacher);
        userRepository.save(teacher.getUser());
        CreateTeacherRequest requestWithTheSameEmail = TeacherFactory.aCreateTeacherRequestWithEmail(TEACHER_EMAIL);


        // when
        EmailAlreadyExistsException exception = assertThrows(
                EmailAlreadyExistsException.class,
                () -> adminTeacherService.createTeacher(requestWithTheSameEmail)
        );

        // then
        assertThat(exception.getMessage()).isEqualTo("Email: teacher@gmail.com already in use.");
    }

    @Test
    void canCreateTeacher() {
        // given
        CreateTeacherRequest request = TeacherFactory.aCreateTeacherRequestWithEmail(TEACHER_EMAIL);

        // when
        String teacherId = adminTeacherService.createTeacher(request);

        // then
        TeacherEntity savedTeacher = teacherRepository.getById(UUID.fromString(teacherId));
        assertThat(savedTeacher)
                .isNotNull()
                .hasFieldOrPropertyWithValue("user.email", TEACHER_EMAIL)
                .extracting("user.passwordConfirmationToken").isNotNull();
    }
}
