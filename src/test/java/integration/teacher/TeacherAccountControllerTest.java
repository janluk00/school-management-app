package integration.teacher;

import com.janluk.schoolmanagementapp.SchoolmanagementappApplication;
import com.janluk.schoolmanagementapp.common.model.vo.GradeType;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.common.schema.CourseDTO;
import com.janluk.schoolmanagementapp.teacher.schema.AddGradeRequest;
import com.janluk.schoolmanagementapp.teacher.schema.CreateGradeResponse;
import com.janluk.schoolmanagementapp.teacher.schema.StudentPerformanceReportDTO;
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
import java.util.UUID;

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
public class TeacherAccountControllerTest extends AbstractPostgresContainerTest {

    private static final String TEACHER_1_EMAIL = "oldman@gmail.com";
    private static final UUID STUDENT_1_UUID = UUID.fromString("86110bd0-d7d6-4b5d-856c-dcdbc9fe94ba");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnTwoCoursesAssignedToTeacher() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(API_PREFIX + TEACHERS_PREFIX)
                .path("/courses")
                .build()
                .toUri();

        // when
        ResponseEntity<List<CourseDTO>> response = restTemplate
                .withBasicAuth(TEACHER_1_EMAIL, COMMON_PASSWORD)
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
    void shouldReturnStudentPerformanceReport() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(API_PREFIX + TEACHERS_PREFIX)
                .path("/reports")
                .build()
                .toUri();

        // when
        ResponseEntity<List<StudentPerformanceReportDTO>> response = restTemplate
                .withBasicAuth(TEACHER_1_EMAIL, COMMON_PASSWORD)
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

        List<StudentPerformanceReportDTO> body = response.getBody();

        assertThat(body)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .extracting(
                        StudentPerformanceReportDTO::schoolClass,
                        StudentPerformanceReportDTO::schoolSubject,
                        StudentPerformanceReportDTO::gradePointAverage
                )
                .containsExactlyInAnyOrder(
                        tuple("C3", "MATHEMATICS", BigDecimal.valueOf(2.75)),
                        tuple("C3", "PHYSICS", BigDecimal.valueOf(2.75))
                );
    }

    @Test
    void shouldThrowBadRequestWhenAddingGradeToUnassignedCourse() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(API_PREFIX + TEACHERS_PREFIX)
                .path("/students" + "/" + STUDENT_1_UUID)
                .path("/grades")
                .build()
                .toUri();
        var addGradeRequest = new AddGradeRequest(GradeType.A, SubjectType.ENGLISH);

        // when
        ResponseEntity<CreateGradeResponse> response = restTemplate
                .withBasicAuth(TEACHER_1_EMAIL, COMMON_PASSWORD)
                .postForEntity(
                uri,
                addGradeRequest,
                CreateGradeResponse.class
        );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldAddGrade() {
        // given
        URI uri = UriComponentsBuilder.fromUriString(API_PREFIX + TEACHERS_PREFIX)
                .path("/students" + "/")
                .path(STUDENT_1_UUID.toString())
                .path("/grades")
                .build()
                .toUri();
        var addGradeRequest = new AddGradeRequest(GradeType.A, SubjectType.MATHEMATICS);

        // when
        ResponseEntity<CreateGradeResponse> response = restTemplate
                .withBasicAuth(TEACHER_1_EMAIL, COMMON_PASSWORD)
                .postForEntity(
                        uri,
                        addGradeRequest,
                        CreateGradeResponse.class
                );

        // then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.CREATED)
                .hasFieldOrProperty("body")
                .extracting(ResponseEntity::getBody)
                .hasFieldOrProperty("gradeId");
    }
}
