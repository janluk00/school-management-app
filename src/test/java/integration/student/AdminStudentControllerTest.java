package integration.student;

import com.janluk.schoolmanagementapp.SchoolmanagementappApplication;
import com.janluk.schoolmanagementapp.common.schema.StudentDTO;
import com.janluk.schoolmanagementapp.student.schema.CreateStudentResponse;
import factory.StudentFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;
import utils.AbstractPostgresContainerTest;
import utils.CustomizedPage;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static utils.CommonPrefixes.*;
import static utils.CommonTestData.*;

@SpringBootTest(
        classes = SchoolmanagementappApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = {"classpath:sql/student/AdminStudentControllerTestCleanUp.sql"}, executionPhase = AFTER_TEST_METHOD)
@Sql(value = {"classpath:sql/student/AdminStudentControllerTestSetUp.sql"})
class AdminStudentControllerTest extends AbstractPostgresContainerTest {

    private static final UUID STUDENT_1_UUID = UUID.fromString("03829453-2eae-4555-9bce-8daf6ff13182");
    private static final UUID USER_STUDENT_1_UUID = UUID.fromString("bea87528-af06-4c52-9085-3938fe8cd4c5");
    private static final UUID STUDENT_2_UUID = UUID.fromString("deb99863-2e4f-4556-9b6c-c024788c96f5");
    private static final UUID STUDENT_3_UUID = UUID.fromString("86110bd0-d7d6-4b5d-856c-dcdbc9fe94ba");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateStudent() {
        // given
        var createStudentRequest = StudentFactory.aCreateStudentRequestWithEmail("kevin@gmail.com");

        // when
        ResponseEntity<CreateStudentResponse> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .postForEntity(
                ADMIN_RESOURCES_PREFIX + STUDENTS_PREFIX,
                createStudentRequest,
                CreateStudentResponse.class
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.CREATED)
                .hasFieldOrProperty("body")
                .extracting(ResponseEntity::getBody)
                .hasFieldOrProperty("studentId");
    }

    @Test
    void shouldThrowExceptionWhenCreatingStudentWithExistingEmail() {
        // given
        var createStudentRequest = StudentFactory.aCreateStudentRequestWithEmail("shawshank@gmail.com");

        // when
        ResponseEntity<CreateStudentResponse> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .postForEntity(
                ADMIN_RESOURCES_PREFIX + STUDENTS_PREFIX,
                createStudentRequest,
                CreateStudentResponse.class
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.CONFLICT);
    }

    @Test
    void shouldFindTwoStudentsBySurname() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + STUDENTS_PREFIX)
                .queryParam("surname", "jackson")
                .queryParam("page", "0")
                .queryParam("size", "10")
                .build()
                .toUri();

        // when
        ResponseEntity<CustomizedPage<StudentDTO>> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.OK)
                .hasFieldOrProperty("body");

        CustomizedPage<StudentDTO> body = response.getBody();

        assertThat(body)
                .isNotNull();

        assertThat(body.getContent())
                .isNotEmpty()
                .hasSize(2)
                .extracting(
                        student -> student.user().surname(),
                        student -> student.user().name(),
                        student -> student.user().birthDate(),
                        student -> student.schoolClass(),
                        student -> student.id()
                )
                .containsExactlyInAnyOrder(
                        tuple("Jackson", "Michael", LocalDate.of(1958, 8, 29), "A2", STUDENT_1_UUID),
                        tuple("Jackson", "Ellis", LocalDate.of(1937,6,1), "C3", STUDENT_2_UUID)
                );
    }

    @Test
    void shouldReturnStudentWhenFoundById() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + STUDENTS_PREFIX + "/")
                .path(STUDENT_1_UUID.toString())
                .build()
                .toUri();

        // when
        ResponseEntity<StudentDTO> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.OK)
                .hasFieldOrProperty("body")
                .extracting(ResponseEntity::getBody)
                .hasFieldOrPropertyWithValue("id", STUDENT_1_UUID)
                .hasFieldOrPropertyWithValue("schoolClass", "A2")
                .hasFieldOrPropertyWithValue("user.id", USER_STUDENT_1_UUID)
                .hasFieldOrPropertyWithValue("user.name", "Michael")
                .hasFieldOrPropertyWithValue("user.surname", "Jackson");
    }

    @Test
    void shouldReturnNotFoundAndStatusWhenStudentDoesNotExist() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + STUDENTS_PREFIX + "/")
                .path(RANDOM_UUID.toString())
                .build()
                .toUri();

        // when
        ResponseEntity<String> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldFindTwoStudentsInGivenClass() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(
                ADMIN_RESOURCES_PREFIX + STUDENTS_PREFIX + SCHOOL_CLASSES_PREFIX + "/"
                )
                .path("C3")
                .build()
                .toUri();

        // when
        ResponseEntity<List<StudentDTO>> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.OK)
                .hasFieldOrProperty("body");

        List<StudentDTO> body = response.getBody();

        assertThat(body)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .extracting(
                        student -> student.user().surname(),
                        student -> student.user().name(),
                        student -> student.user().birthDate(),
                        student -> student.schoolClass(),
                        student -> student.id()
                )
                .containsExactlyInAnyOrder(
                        tuple("Jackson", "Ellis", LocalDate.of(1937, 6, 1), "C3", STUDENT_2_UUID),
                        tuple("Dufresne", "Andy", LocalDate.of(1958,10,16), "C3", STUDENT_3_UUID)
                );
    }

}
