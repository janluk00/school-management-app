package repository;

import com.janluk.schoolmanagementapp.common.exception.NoResultFoundException;
import com.janluk.schoolmanagementapp.common.model.SchoolSubjectEntity;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.common.repository.port.SchoolSubjectRepository;
import com.janluk.schoolmanagementapp.common.schema.TaughtSubjectDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemorySchoolSubjectRepository implements SchoolSubjectRepository {

    private Map<String, SchoolSubjectEntity> schoolSubjects;

    public InMemorySchoolSubjectRepository() {

        schoolSubjects = Stream.of(SubjectType.values())
                .collect(Collectors.toMap(Enum::name, subjectType -> SchoolSubjectEntity.builder()
                        .name(subjectType.name())
                        .courses(new HashSet<>())
                        .build()
                    )
                );
    }

    @Override
    public SchoolSubjectEntity getById(SubjectType subject) {
        SchoolSubjectEntity schoolSubject = schoolSubjects.get(subject.name());

        if (schoolSubject == null) {
            throw new NoResultFoundException(
                    "Could not find school subject with name: %s".formatted(subject.name())
            );
        }

        return schoolSubject;
    }

    @Override
    public Page<SchoolSubjectEntity> getAll(Pageable pageable) {
        return new PageImpl<>(schoolSubjects.values().stream().toList(), pageable, 50);
    }

    @Override
    public List<TaughtSubjectDTO> getAllTaughtSubjectsInSchoolClass(String schoolClass) {
        return schoolSubjects.values().stream()
                .flatMap(schoolSubject -> schoolSubject.getCourses().stream()
                        .flatMap(course -> course.getSchoolClasses().stream()
                                .filter(sClass -> sClass.getName().equals(schoolClass))
                                .map(schoolClass1 -> new TaughtSubjectDTO(
                                        schoolSubject.getName(),
                                        course.getTeacher().getUser().getName(),
                                        course.getTeacher().getUser().getSurname())))
                )
                .distinct()
                .toList();
    }

    public void updateSchoolSubjects(Set<SchoolSubjectEntity> subjects) {
        if (subjects == null || subjects.isEmpty()) {
            return;
        }

        for (SchoolSubjectEntity newSubject : subjects) {
            schoolSubjects.put(newSubject.getName(), newSubject);
        }
    }

    public void deleteAll() {
        schoolSubjects = Stream.of(SubjectType.values())
                .collect(Collectors.toMap(Enum::name, subjectType -> new SchoolSubjectEntity(subjectType.name())));
    }
}

