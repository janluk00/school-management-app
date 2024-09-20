package repository;

import com.janluk.schoolmanagementapp.common.model.BaseEntity;
import com.janluk.schoolmanagementapp.common.model.GradeEntity;
import com.janluk.schoolmanagementapp.common.model.StudentEntity;
import com.janluk.schoolmanagementapp.common.repository.port.GradeRepository;
import com.janluk.schoolmanagementapp.student.schema.GradeDTO;
import com.janluk.schoolmanagementapp.student.schema.SchoolSubjectGradePointAverageDTO;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InMemoryGradeRepository implements GradeRepository {

    private Map<UUID, StudentEntity> students = new HashMap<>();

    @Override
    public List<GradeDTO> getAllGradesByStudent(String email) {
        StudentEntity student = students.values().stream()
                .filter(s -> s.getUser().getEmail().equals(email))
                .findFirst()
                .orElse(null);

        if (student == null) {
            return Collections.emptyList();
        }

        return student.getGrades().stream()
                .map(grade -> new GradeDTO(grade.getSubjectName(), grade.getGrade()))
                .toList();
    }

    @Override
    public List<SchoolSubjectGradePointAverageDTO> getGradePointAveragesByStudent(String email) {
        StudentEntity student = students.values().stream()
                .filter(s -> s.getUser().getEmail().equals(email))
                .findFirst()
                .orElse(null);

        if (student == null) {
            return Collections.emptyList();
        }

        return student.getGrades().stream()
                .collect(
                        Collectors.groupingBy(
                                GradeEntity::getSubjectName,
                                Collectors.averagingDouble(value -> value.getGrade().doubleValue())
                                )
                )
                .entrySet().stream()
                .map(entry -> new SchoolSubjectGradePointAverageDTO(entry.getKey(), entry.getValue()))
                .toList();
    }

    public void initializeWithStudents(Set<StudentEntity> studentEntities) {
        students = studentEntities.stream()
                .collect(Collectors.toMap(BaseEntity::getId, Function.identity()));
    }

    public void deleteAll() {
        students.clear();
    }
}
