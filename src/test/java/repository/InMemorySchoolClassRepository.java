package repository;

import com.janluk.schoolmanagementapp.common.exception.NoResultFoundException;
import com.janluk.schoolmanagementapp.common.model.SchoolClassEntity;
import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.repository.port.SchoolClassRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemorySchoolClassRepository implements SchoolClassRepository {

    private final Map<String, SchoolClassEntity> schoolClasses;

    public InMemorySchoolClassRepository() {
        schoolClasses = Stream.of(ClassType.values())
                .collect(
                        Collectors.toMap(
                                Enum::name,
                                classType -> new SchoolClassEntity(
                                        classType.name(),
                                        new HashSet<>(),
                                        new HashSet<>()
                                )
                        )
                );
    }

    @Override
    public SchoolClassEntity getById(ClassType schoolClass) {
        SchoolClassEntity s = schoolClasses.get(schoolClass.name());

        if (s == null) {
            throw new NoResultFoundException(
                    "Could not find school class with name: %s".formatted(schoolClass.name())
            );
        }

        return s;
    }

    @Override
    public Page<SchoolClassEntity> getAll(Pageable pageable) {
        return new PageImpl<>(schoolClasses.values().stream().toList(), pageable, 50);
    }

    public void updateSchoolClass(SchoolClassEntity schoolClass) {
        schoolClasses.replace(schoolClass.getName(), new SchoolClassEntity(
                schoolClass.getName(),
                schoolClass.getStudents(),
                schoolClass.getCourses()
                )
        );
    }
}
