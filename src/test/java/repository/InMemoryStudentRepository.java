package repository;

import com.janluk.schoolmanagementapp.common.exception.NoResultFoundException;
import com.janluk.schoolmanagementapp.common.model.StudentEntity;
import com.janluk.schoolmanagementapp.common.repository.port.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InMemoryStudentRepository implements StudentRepository {

    private final Map<UUID, StudentEntity> students = new HashMap<>();

    @Override
    public StudentEntity getById(UUID id) {
        StudentEntity student = students.get(id);

        if (student == null) {
            throw new NoResultFoundException(
                    "Could not find student with id: %s".formatted(id.toString())
            );
        }

        return student;
    }

    @Override
    public String getStudentSchoolClassByEmail(String email) {
        return students.values().stream()
                .filter(s -> s.getUser().getEmail().equals(email))
                .findFirst()
                .map(student -> student.getSchoolClass().getName())
                .orElseThrow(() -> new NoResultFoundException("Could not find student with email: %s".formatted(email)));
    }

    @Override
    public List<StudentEntity> getAllInSchoolClass(String schoolClass) {
        return students.values().stream()
                .filter(student -> student.getSchoolClass().getName().equals(schoolClass))
                .toList();
    }

    @Override
    public Page<StudentEntity> getAll(Specification<StudentEntity> spec, Pageable pageable) {
        throw new NotImplementedTestMethodException();
    }

    @Override
    public UUID save(StudentEntity student) {
        UUID studentId = student.getId();

        students.put(studentId, student);

        return studentId;
    }

    public void deleteAll() {
        students.clear();
    }
}
