package unit.schoolClass;

import com.janluk.schoolmanagementapp.common.repository.port.SchoolClassRepository;
import com.janluk.schoolmanagementapp.schoolClass.mapper.SchoolClassMapper;
import com.janluk.schoolmanagementapp.schoolClass.schema.SchoolClassDTO;
import com.janluk.schoolmanagementapp.schoolClass.service.AdminSchoolClassService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import repository.InMemorySchoolClassRepository;

import static org.assertj.core.api.Assertions.assertThat;

public class AdminSchoolClassServiceTest {

    private final SchoolClassRepository schoolClassRepository = new InMemorySchoolClassRepository();
    private final SchoolClassMapper schoolClassMapper = new SchoolClassMapper();

    private final AdminSchoolClassService adminSchoolClassService = new AdminSchoolClassService(
            schoolClassRepository,
            schoolClassMapper
    );

    @Test
    void shouldReturnPageOfSchoolClasses() {
        // given
        Pageable pageable = PageRequest.of(1, 10);

        // when
        Page<SchoolClassDTO> classDTOPage = adminSchoolClassService.getAllSchoolClasses(pageable);

        // then
        assertThat(classDTOPage).isNotNull();
        assertThat(classDTOPage).hasSize(9);
    }
}
