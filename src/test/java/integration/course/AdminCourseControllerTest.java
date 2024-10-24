package integration.course;

import com.janluk.schoolmanagementapp.SchoolmanagementappApplication;
import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.course.schema.AssignTeacherToCourseResponse;
import com.janluk.schoolmanagementapp.course.schema.RemoveTeacherFromCourseResponse;
import factory.CourseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;
import utils.AbstractPostgresContainerTest;


import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static utils.CommonPrefixes.ADMIN_RESOURCES_PREFIX;
import static utils.CommonTestData.ADMIN_EMAIL;
import static utils.CommonTestData.ADMIN_PASSWORD;

@SpringBootTest(
        classes = SchoolmanagementappApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = {"classpath:sql/common/CoursesAndTeachersTestSetUp.sql"})
@Sql(value = {"classpath:sql/common/CoursesAndTeachersCleanUp.sql"}, executionPhase = AFTER_TEST_METHOD)
class AdminCourseControllerTest extends AbstractPostgresContainerTest {

    private static final UUID TEACHER_1_UUID = UUID.fromString("097eaa63-9995-45cb-be07-9f34401c5808");
    private static final UUID TEACHER_3_UUID = UUID.fromString("7b236ff9-e134-420e-bce5-b79da1bb419d");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldAssignTeacherToCourse() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + "/courses")
                .build()
                .toUri();

        var assignTeacherToCourseRequest = CourseFactory.aAssignTeacherToCourseRequest(
                TEACHER_3_UUID,
                SubjectType.CHEMISTRY
        );

        // when
        ResponseEntity<AssignTeacherToCourseResponse> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .postForEntity(
                uri,
                assignTeacherToCourseRequest,
                AssignTeacherToCourseResponse.class
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.OK)
                .extracting(ResponseEntity::getBody)
                .hasFieldOrProperty("courseId");
    }

    @Test
    void shouldThrowConflictWhenAssigningTeacherToSubjectAlreadyTaughtInClass() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + "/courses")
                .build()
                .toUri();

        var assignTeacherToCourseRequest = CourseFactory.aAssignTeacherToCourseRequest(
                TEACHER_3_UUID,
                SubjectType.ENGLISH
        );

        // when
        ResponseEntity<AssignTeacherToCourseResponse> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .postForEntity(
                uri,
                assignTeacherToCourseRequest,
                AssignTeacherToCourseResponse.class
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.CONFLICT);
    }

    @Test
    void shouldThrowBadRequestWhenAssigningTeacherWhoDoesNotTeachSubject() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + "/courses")
                .build()
                .toUri();

        var assignTeacherToCourseRequest = CourseFactory.aAssignTeacherToCourseRequest(
                TEACHER_3_UUID,
                SubjectType.MATHEMATICS
        );

        // when
        ResponseEntity<AssignTeacherToCourseResponse> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .postForEntity(
                uri,
                assignTeacherToCourseRequest,
                AssignTeacherToCourseResponse.class
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldRemoveTeacherFromAssignedCourse() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + "/courses")
                .build()
                .toUri();

        var removeTeacherFromCourseRequest = CourseFactory.aRemoveTeacherFromCourseRequest(
                TEACHER_1_UUID,
                ClassType.C3
        );

        // when
        ResponseEntity<RemoveTeacherFromCourseResponse> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .exchange(
                uri,
                HttpMethod.DELETE,
                new HttpEntity<>(removeTeacherFromCourseRequest),
                new ParameterizedTypeReference<>() {}
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.OK)
                .extracting(ResponseEntity::getBody)
                .hasFieldOrProperty("courseId");
    }

    @Test
    void shouldThrowConflictWhenRemovingTeacherNotAssignedToCourseInClass() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + "/courses")
                .build()
                .toUri();

        var removeTeacherFromCourseRequest = CourseFactory.aRemoveTeacherFromCourseRequest(
                TEACHER_1_UUID,
                ClassType.A1
        );

        // when
        ResponseEntity<RemoveTeacherFromCourseResponse> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .exchange(
                uri,
                HttpMethod.DELETE,
                new HttpEntity<>(removeTeacherFromCourseRequest),
                new ParameterizedTypeReference<>() {}
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.CONFLICT);
    }
}
