package integration.schoolClass;

import com.janluk.schoolmanagementapp.SchoolmanagementappApplication;
import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.schoolClass.schema.SchoolClassDTO;
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
class AdminSchoolClassControllerTest extends AbstractPostgresContainerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnPagedSchoolClasses() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(ADMIN_RESOURCES_PREFIX + SCHOOL_CLASSES_PREFIX)
                .queryParam("page", "0")
                .queryParam("size", "30")
                .build()
                .toUri();

        // when
        ResponseEntity<CustomizedPage<SchoolClassDTO>> response = restTemplate
                .withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
                .exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        CustomizedPage<SchoolClassDTO> body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.getContent()).isNotEmpty();
        assertThat(body.getNumberOfElements()).isGreaterThan(0);

        List<String> schoolClassesResponse = body.getContent().stream()
                .map(SchoolClassDTO::name)
                .toList();

        List<String> validSchoolClasses = Arrays.stream(ClassType.values())
                .map(Enum::name)
                .toList();

        assertThat(schoolClassesResponse).allMatch(validSchoolClasses::contains);
        assertThat(validSchoolClasses).containsAll(schoolClassesResponse);
    }
}
