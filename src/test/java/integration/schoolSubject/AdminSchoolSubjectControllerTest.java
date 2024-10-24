package integration.schoolSubject;

import com.janluk.schoolmanagementapp.SchoolmanagementappApplication;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.common.schema.TaughtSubjectDTO;
import com.janluk.schoolmanagementapp.schoolSubject.schema.SchoolSubjectDTO;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;
import utils.AbstractPostgresContainerTest;
import utils.CustomizedPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static utils.CommonPrefixes.*;
import static utils.CommonTestData.ADMIN_EMAIL;
import static utils.CommonTestData.ADMIN_PASSWORD;

@SpringBootTest(
        classes = SchoolmanagementappApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = {"classpath:sql/common/CoursesAndTeachersTestSetUp.sql"})
@Sql(value = {"classpath:sql/common/CoursesAndTeachersCleanUp.sql"}, executionPhase = AFTER_TEST_METHOD)
class AdminSchoolSubjectControllerTest extends AbstractPostgresContainerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String C3_SCHOOL_CLASS = "C3";

    @Test
    void shouldReturnPagedSchoolSubjects() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + SCHOOL_SUBJECTS_PREFIX)
                .queryParam("page", "0")
                .queryParam("size", "20")
                .build()
                .toUri();

        // when
        ResponseEntity<CustomizedPage<SchoolSubjectDTO>> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        CustomizedPage<SchoolSubjectDTO> body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.getContent()).isNotEmpty();
        assertThat(body.getNumberOfElements()).isGreaterThan(0);

        List<String> subjectsInResponse = body.getContent().stream()
                .map(SchoolSubjectDTO::name)
                .toList();

        List<String> validSubjects = Arrays.stream(SubjectType.values())
                .map(Enum::name)
                .toList();

        assertThat(subjectsInResponse).allMatch(validSubjects::contains);
        assertThat(validSubjects).containsAll(subjectsInResponse);
    }

    @Test
    void shouldReturnTwoCoursesAssignedToSchoolClass() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + SCHOOL_SUBJECTS_PREFIX + "/")
                .path("school-classes")
                .path("/" + C3_SCHOOL_CLASS)
                .path("/courses")
                .build()
                .toUri();

        // when
        ResponseEntity<List<TaughtSubjectDTO>> response = restTemplate
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

        List<TaughtSubjectDTO> body = response.getBody();

        assertThat(body)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .extracting(
                        TaughtSubjectDTO::subjectName,
                        TaughtSubjectDTO::teacherName,
                        TaughtSubjectDTO::teacherSurname
                )
                .containsExactlyInAnyOrder(
                        tuple("MATHEMATICS", "Gary", "Oldman"),
                        tuple("PHYSICS", "Gary", "Oldman")
                );
    }
}
