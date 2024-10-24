package unit.student;

import com.janluk.schoolmanagementapp.common.email.EmailService;
import com.janluk.schoolmanagementapp.common.exception.EmailAlreadyExistsException;
import com.janluk.schoolmanagementapp.common.exception.NoResultFoundException;
import com.janluk.schoolmanagementapp.common.model.StudentEntity;
import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.repository.port.RoleRepository;
import com.janluk.schoolmanagementapp.common.schema.StudentDTO;
import com.janluk.schoolmanagementapp.common.user.RoleAdder;
import com.janluk.schoolmanagementapp.common.user.UserValidator;
import com.janluk.schoolmanagementapp.student.criteria.StudentSearcher;
import com.janluk.schoolmanagementapp.student.mapper.StudentMapper;
import com.janluk.schoolmanagementapp.student.schema.CreateStudentRequest;
import com.janluk.schoolmanagementapp.student.schema.CreateStudentResponse;
import com.janluk.schoolmanagementapp.student.service.AdminStudentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import factory.StudentFactory;
import repository.FakeTestEmailService;
import repository.InMemoryRoleRepository;
import repository.InMemoryStudentRepository;
import repository.InMemoryUserRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdminStudentServiceTest {

    private final RoleRepository roleRepository = new InMemoryRoleRepository();
    private final InMemoryUserRepository userRepository = new InMemoryUserRepository();
    private final UserValidator userValidator = new UserValidator(userRepository);
    private final StudentMapper studentMapper = new StudentMapper();
    private final RoleAdder roleAdder = new RoleAdder(roleRepository);
    private final InMemoryStudentRepository studentRepository = new InMemoryStudentRepository();
    private final StudentSearcher studentSearcher = new StudentSearcher(studentRepository);
    private final EmailService emailService = new FakeTestEmailService();

    private final AdminStudentService adminStudentService = new AdminStudentService(
            userValidator,
            studentMapper,
            roleAdder,
            studentRepository,
            studentSearcher,
            emailService
    );

    private static final String STUDENT_EMAIL = "student@gmail.com";
    private static final String STUDENT_EMAIL2 = "student2@gmail.com";
    private static final String STUDENT_EMAIL3 = "student3@gmail.com";
    private static final UUID RANDOM_UUID = UUID.randomUUID();

    @AfterEach
    void clean() {
        userRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    void shouldThrowExceptionWhenStudentNotFoundById() {
        // given
        StudentEntity student = StudentFactory.aStudentWithUser(STUDENT_EMAIL);
        studentRepository.save(student);

        // when
        NoResultFoundException noResultFoundException = assertThrows(
                NoResultFoundException.class,
                () -> adminStudentService.getStudentById(RANDOM_UUID)
        );

        // then
        assertEquals(
                noResultFoundException.getMessage(),
                "Could not find student with id: %s".formatted(RANDOM_UUID.toString())
        );
    }

    @Test
    void shouldReturnStudentWhenFoundById() {
        // given
        StudentEntity student = StudentFactory.aStudentWithUser(STUDENT_EMAIL);
        UUID studentId = student.getId();
        studentRepository.save(student);

        // when
        StudentDTO studentDTO = adminStudentService.getStudentById(studentId);

        // then
        assertThat(studentDTO)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", studentId);
    }

    @Test
    void shouldThrowExceptionWhenCreatingStudentWithExistingEmail() {
        // given
        StudentEntity student = StudentFactory.aStudentWithUser(STUDENT_EMAIL);
        studentRepository.save(student);
        userRepository.save(student.getUser());
        CreateStudentRequest requestWithTheSameEmail = StudentFactory.aCreateStudentRequestWithEmail(STUDENT_EMAIL);

        // when
        EmailAlreadyExistsException exception = assertThrows(
                EmailAlreadyExistsException.class,
                () -> adminStudentService.createStudent(requestWithTheSameEmail)
        );

        // then
        assertEquals(exception.getMessage(), "Email: student@gmail.com already in use.");
    }

    @Test
    void shouldReturnAllStudentsInSpecifiedClass() {
        // given
        StudentEntity student1 = StudentFactory.aStudentWithUserInClass(STUDENT_EMAIL, ClassType.A1);
        StudentEntity student2 = StudentFactory.aStudentWithUserInClass(STUDENT_EMAIL2, ClassType.A1);
        StudentEntity student3 = StudentFactory.aStudentWithUserInClass(STUDENT_EMAIL3, ClassType.C2);
        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);

        // when
        List<StudentDTO> studentDTOS = adminStudentService.getAllStudentsInClass(ClassType.A1);

        // then
        assertThat(studentDTOS)
                .hasSize(2)
                .extracting(StudentDTO::id)
                .containsOnly(student1.getId(), student2.getId())
                .doesNotContain(student3.getId());
    }

    @Test
    void shouldCreateStudent() {
        // given
        CreateStudentRequest request = StudentFactory.aCreateStudentRequestWithEmail(STUDENT_EMAIL);

        // when
        CreateStudentResponse response = adminStudentService.createStudent(request);

        // then
        StudentEntity savedStudent = studentRepository.getById(UUID.fromString(response.studentId()));
        assertThat(savedStudent)
                .isNotNull()
                .hasFieldOrPropertyWithValue("user.email", STUDENT_EMAIL)
                .extracting("user.passwordConfirmationToken").isNotNull();
    }
}
