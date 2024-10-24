package integration.student;

import com.janluk.schoolmanagementapp.SchoolmanagementappApplication;
import com.janluk.schoolmanagementapp.common.schema.TaughtSubjectDTO;
import com.janluk.schoolmanagementapp.student.schema.SchoolSubjectGradePointAverageDTO;
import com.janluk.schoolmanagementapp.student.schema.SchoolSubjectGradesDTO;
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

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static utils.CommonPrefixes.*;
import static utils.CommonTestData.COMMON_PASSWORD;

@SpringBootTest(
        classes = SchoolmanagementappApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = {"classpath:sql/common/SeedAllData.sql"})
@Sql(value = {"classpath:sql/common/CleanAllData.sql"}, executionPhase = AFTER_TEST_METHOD)
public class StudentAccountControllerTest extends AbstractPostgresContainerTest {

    private static final String STUDENT_1_EMAIL = "shawshank@gmail.com";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnGroupedGradesBySchoolSubject() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(API_PREFIX + STUDENTS_PREFIX)
                .path("/grades")
                .build()
                .toUri();

        // when
        ResponseEntity<List<SchoolSubjectGradesDTO>> response = restTemplate
                .withBasicAuth(STUDENT_1_EMAIL, COMMON_PASSWORD)
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

        List<SchoolSubjectGradesDTO> body = response.getBody();

        Map<String, List<BigDecimal>> expectedGrades = Map.of(
                "MATHEMATICS", List.of(new BigDecimal("2.25"), new BigDecimal("1.50")),
                "PHYSICS", List.of(new BigDecimal("2.25"), new BigDecimal("2.25"))
        );

        assertThat(body)
                .extracting(SchoolSubjectGradesDTO::schoolSubject)
                .containsExactlyInAnyOrderElementsOf(expectedGrades.keySet());

        assertThat(body)
                .allSatisfy(subjectGradeDTO -> {
                    String subject = subjectGradeDTO.schoolSubject();
                    List<BigDecimal> actualGrades = subjectGradeDTO.grades();
                    List<BigDecimal> expectedGradesForSubject = expectedGrades.get(subject);

                    assertThat(actualGrades)
                            .containsExactlyInAnyOrderElementsOf(expectedGradesForSubject);
                });
    }

    @Test
    void shouldReturnGroupedGPABySchoolSubject() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(API_PREFIX + STUDENTS_PREFIX)
                .path("/grades")
                .path("/averages")
                .build()
                .toUri();

        // when
        ResponseEntity<List<SchoolSubjectGradePointAverageDTO>> response = restTemplate
                .withBasicAuth(STUDENT_1_EMAIL, COMMON_PASSWORD)
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

        List<SchoolSubjectGradePointAverageDTO> body = response.getBody();

        assertThat(body)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .extracting(
                        SchoolSubjectGradePointAverageDTO::schoolSubject,
                        SchoolSubjectGradePointAverageDTO::gradePointAverage
                )
                .containsExactlyInAnyOrder(
                        tuple("MATHEMATICS", new BigDecimal("1.875")),
                        tuple("PHYSICS", new BigDecimal("2.25"))
                );
    }

    @Test
    void shouldReturnCoursesAssignedToStudent() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(API_PREFIX + STUDENTS_PREFIX)
                .path("/courses")
                .build()
                .toUri();

        // when
        ResponseEntity<List<TaughtSubjectDTO>> response = restTemplate
                .withBasicAuth(STUDENT_1_EMAIL, COMMON_PASSWORD)
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
