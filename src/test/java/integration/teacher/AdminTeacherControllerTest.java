package integration.teacher;

import com.janluk.schoolmanagementapp.SchoolmanagementappApplication;
import com.janluk.schoolmanagementapp.common.schema.CourseDTO;
import com.janluk.schoolmanagementapp.teacher.schema.*;
import factory.SchoolClassFactory;
import factory.SchoolSubjectFactory;
import factory.TeacherFactory;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static utils.CommonPrefixes.*;
import static utils.CommonTestData.*;

@SpringBootTest(
        classes = SchoolmanagementappApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = {"classpath:sql/common/CoursesAndTeachersTestSetUp.sql"})
@Sql(value = {"classpath:sql/common/CoursesAndTeachersCleanUp.sql"}, executionPhase = AFTER_TEST_METHOD)
class AdminTeacherControllerTest extends AbstractPostgresContainerTest {

    private static final UUID TEACHER_1_UUID = UUID.fromString("097eaa63-9995-45cb-be07-9f34401c5808");
    private static final UUID USER_TEACHER_1_UUID = UUID.fromString("d445aba7-56f0-4033-b8df-099f5de08eec");
    private static final UUID TEACHER_2_UUID = UUID.fromString("60070625-5eff-4fa1-811f-f6aea73a82e6");
    private static final UUID TEACHER_3_UUID = UUID.fromString("7b236ff9-e134-420e-bce5-b79da1bb419d");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldFindTwoTeachersBySurname() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + TEACHERS_PREFIX)
                .queryParam("surname", "o")
                .queryParam("page", "0")
                .queryParam("size", "10")
                .build()
                .toUri();

        // when
        ResponseEntity<CustomizedPage<TeacherDTO>> response = restTemplate
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

        CustomizedPage<TeacherDTO> body = response.getBody();

        assertThat(body)
                .isNotNull();

        assertThat(body.getContent())
                .isNotEmpty()
                .hasSize(2)
                .extracting(
                        teacher -> teacher.user().surname(),
                        teacher -> teacher.user().name(),
                        teacher -> teacher.user().birthDate(),
                        teacher -> teacher.id()
                )
                .containsExactlyInAnyOrder(
                        tuple("Oldman", "Gary", LocalDate.of(1958, 3, 21), TEACHER_1_UUID),
                        tuple("Reno", "Jean", LocalDate.of(1948, 7, 30), TEACHER_2_UUID)
                );
    }

    @Test
    void shouldReturnTeacherWhenFoundById() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + TEACHERS_PREFIX + "/")
                .path(TEACHER_1_UUID.toString())
                .build()
                .toUri();

        // when
        ResponseEntity<TeacherDTO> response = restTemplate
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
                .hasFieldOrPropertyWithValue("id", TEACHER_1_UUID)
                .hasFieldOrPropertyWithValue("user.id", USER_TEACHER_1_UUID)
                .hasFieldOrPropertyWithValue("user.name", "Gary")
                .hasFieldOrPropertyWithValue("user.surname", "Oldman");
    }

    @Test
    void shouldReturnNotFoundAndStatusWhenTeacherDoesNotExist() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + TEACHERS_PREFIX + "/")
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
    void shouldReturnTwoCoursesAssignedToTeacher() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + TEACHERS_PREFIX + "/")
                .path(TEACHER_1_UUID.toString())
                .path("/courses")
                .build()
                .toUri();

        // when
        ResponseEntity<List<CourseDTO>> response = restTemplate
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

        List<CourseDTO> body = response.getBody();

        assertThat(body)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .extracting(
                        CourseDTO::className,
                        CourseDTO::schoolSubject
                )
                .containsExactlyInAnyOrder(
                        tuple("C3", "MATHEMATICS"),
                        tuple("C3", "PHYSICS")
                );
    }

    @Test
    void shouldCreateTeacher() {
        // given
        var createTeacherRequest = TeacherFactory.aCreateTeacherRequestWithEmail("newteacher@gmail.com");

        // when
        ResponseEntity<CreateTeacherResponse> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .postForEntity(
                ADMIN_RESOURCES_PREFIX + TEACHERS_PREFIX,
                createTeacherRequest,
                CreateTeacherResponse.class
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.CREATED)
                .hasFieldOrProperty("body")
                .extracting(ResponseEntity::getBody)
                .hasFieldOrProperty("teacherId");
    }

    @Test
    void shouldThrowExceptionWhenCreatingTeacherWithExistingEmail() {
        // given
        var createTeacherRequest = TeacherFactory.aCreateTeacherRequestWithEmail("prot@gmail.com");

        // when
        ResponseEntity<CreateTeacherResponse> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .postForEntity(
                ADMIN_RESOURCES_PREFIX + TEACHERS_PREFIX,
                createTeacherRequest,
                CreateTeacherResponse.class
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.CONFLICT);
    }

    @Test
    void shouldAssignTutorToTeacher() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + TEACHERS_PREFIX + "/")
                .path(TEACHER_1_UUID.toString())
                .path("/tutor")
                .build()
                .toUri();

        var schoolClassRequest = SchoolClassFactory.aSchoolClassRequestA1();

        // when
        ResponseEntity<AssignTutorToTeacherResponse> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .postForEntity(
                uri,
                schoolClassRequest,
                AssignTutorToTeacherResponse.class
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.OK)
                .extracting(ResponseEntity::getBody)
                .hasFieldOrPropertyWithValue("teacherId", TEACHER_1_UUID.toString());
    }

    @Test
    void shouldThrowConflictWhenReassigningSameTutorToTeacher() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + TEACHERS_PREFIX + "/")
                .path(TEACHER_3_UUID.toString())
                .path("/tutor")
                .build()
                .toUri();

        var schoolClassRequest = SchoolClassFactory.aSchoolClassRequestA1();

        // when
        ResponseEntity<AssignTutorToTeacherResponse> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .postForEntity(
                uri,
                schoolClassRequest,
                AssignTutorToTeacherResponse.class
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.CONFLICT);
    }

    @Test
    void shouldAssignSubjectToTeacher() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + TEACHERS_PREFIX + "/")
                .path(TEACHER_1_UUID.toString())
                .path("/school-subjects")
                .build()
                .toUri();

        var schoolSubjectRequest = SchoolSubjectFactory.aSchoolBiologyRequestMath();

        // when
        ResponseEntity<AssignSubjectToTeacherResponse> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .postForEntity(
                uri,
                schoolSubjectRequest,
                AssignSubjectToTeacherResponse.class
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.OK)
                .extracting(ResponseEntity::getBody)
                .hasFieldOrPropertyWithValue("teacherId", TEACHER_1_UUID.toString());
    }

    @Test
    void shouldThrowConflictWhenAssigningSameSubjectToTeacher() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + TEACHERS_PREFIX + "/")
                .path(TEACHER_1_UUID.toString())
                .path("/school-subjects")
                .build()
                .toUri();

        var schoolSubjectRequest = SchoolSubjectFactory.aSchoolSubjectRequestMath();

        // when
        ResponseEntity<AssignSubjectToTeacherResponse> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .postForEntity(
                uri,
                schoolSubjectRequest,
                AssignSubjectToTeacherResponse.class
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldRemoveTutorAssignmentFromTeacher() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + TEACHERS_PREFIX + "/")
                .path(TEACHER_3_UUID.toString())
                .path("/tutor")
                .build()
                .toUri();

        // when
        ResponseEntity<RemoveTutorAssignmentResponse> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .exchange(
                uri,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {}
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.OK)
                .extracting(ResponseEntity::getBody)
                .hasFieldOrPropertyWithValue("teacherId", TEACHER_3_UUID.toString());
    }

    @Test
    void shouldThrowBadRequestWhenRemovingNonAssignedTutor() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + TEACHERS_PREFIX + "/")
                .path(TEACHER_1_UUID.toString())
                .path("/tutor")
                .build()
                .toUri();

        // when
        ResponseEntity<RemoveTutorAssignmentResponse> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .exchange(
                uri,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {}
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldRemoveSubjectFromTeacher() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + TEACHERS_PREFIX + "/")
                .path(TEACHER_1_UUID.toString())
                .path("/school-subjects")
                .queryParam("subject", "MATHEMATICS")
                .build()
                .toUri();

        // when
        ResponseEntity<RemoveSubjectFromTeacherResponse> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .exchange(
                uri,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {}
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.OK)
                .extracting(ResponseEntity::getBody)
                .hasFieldOrPropertyWithValue("teacherId", TEACHER_1_UUID.toString());
    }

    @Test
    void shouldThrowBadRequestWhenRemovingSubjectFromNonExistentTeacher() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + TEACHERS_PREFIX + "/")
                .path(TEACHER_3_UUID.toString())
                .path("/school-subjects")
                .queryParam("subject", "MATHEMATICS")
                .build()
                .toUri();

        // when
        ResponseEntity<RemoveSubjectFromTeacherResponse> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .exchange(
                uri,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {}
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }
}
