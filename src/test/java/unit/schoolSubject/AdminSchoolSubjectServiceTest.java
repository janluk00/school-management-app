package unit.schoolSubject;

import com.janluk.schoolmanagementapp.common.model.SchoolSubjectEntity;
import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.common.schema.TaughtSubjectDTO;
import com.janluk.schoolmanagementapp.schoolSubject.mapper.SchoolSubjectMapper;
import com.janluk.schoolmanagementapp.schoolSubject.schema.SchoolSubjectDTO;
import com.janluk.schoolmanagementapp.schoolSubject.service.AdminSchoolSubjectService;
import factory.SchoolSubjectFactory;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import repository.InMemorySchoolSubjectRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AdminSchoolSubjectServiceTest {

    private final InMemorySchoolSubjectRepository schoolSubjectRepository = new InMemorySchoolSubjectRepository();
    private final SchoolSubjectMapper schoolSubjectMapper = new SchoolSubjectMapper();

    private final AdminSchoolSubjectService adminSchoolSubjectService = new AdminSchoolSubjectService(
            schoolSubjectRepository,
            schoolSubjectMapper
    );

    @Test
    void shouldReturnPageOfSchoolSubjects() {
        // given
        Pageable pageable = PageRequest.of(1, 10);

        // when
        Page<SchoolSubjectDTO> classDTOPage = adminSchoolSubjectService.getAllSchoolSubjects(pageable);

        // then
        assertThat(classDTOPage).isNotNull();
        assertThat(classDTOPage).hasSize(6);
    }

    @Test
    void shouldReturnAllTaughtSubjectsInSchoolClass() {
        // given
        SchoolSubjectEntity mathSubject = SchoolSubjectFactory.aSchoolSubjectWithMathCourse();
        SchoolSubjectEntity englishSubject = SchoolSubjectFactory.aSchoolSubjectWithEnglishCourse();
        Set<SchoolSubjectEntity> subjects = new HashSet<>(Arrays.asList(mathSubject, englishSubject));
        schoolSubjectRepository.updateSchoolSubjects(subjects);

        // when
        List<TaughtSubjectDTO> subjectDTOS = adminSchoolSubjectService.getAllTaughtSubjectsInClass(ClassType.A1);

        // then
        assertThat(subjectDTOS)
                .hasSize(2)
                .extracting(TaughtSubjectDTO::subjectName)
                .containsOnly(SubjectType.MATHEMATICS.name(), SubjectType.ENGLISH.name());
    }
}
