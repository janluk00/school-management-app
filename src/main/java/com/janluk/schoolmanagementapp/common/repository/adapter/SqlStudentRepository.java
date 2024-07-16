package com.janluk.schoolmanagementapp.common.repository.adapter;

import com.janluk.schoolmanagementapp.common.exception.NoResultFoundException;
import com.janluk.schoolmanagementapp.common.model.StudentEntity;
import com.janluk.schoolmanagementapp.common.repository.port.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SqlStudentRepository implements StudentRepository {

    private final JpaStudentRepository jpaStudentRepository;

    @Override
    public StudentEntity getById(UUID id) {
        return jpaStudentRepository.findById(id)
                .orElseThrow(() -> new NoResultFoundException(
                        "Could not find student with id: %s".formatted(id.toString())
                    )
                );
    }

    @Override
    public String getStudentSchoolClassByEmail(String email) {
        return jpaStudentRepository.findStudentSchoolClassByEmail(email)
                .orElseThrow(() -> new NoResultFoundException(
                        "Could not find student with email: %s".formatted(email)
                    )
                );
    }

    @Override
    public List<StudentEntity> getAllInSchoolClass(String schoolClass) {
        return jpaStudentRepository.findAllInSchoolClass(schoolClass);
    }

    @Override
    public List<StudentEntity> getAllInSchoolClassWithGrades(String schoolClass) {
        return jpaStudentRepository.findAllInSchoolClassWithGrades(schoolClass);
    }

    @Override
    public Page<StudentEntity> getAll(Specification<StudentEntity> spec, Pageable pageable) {
        return jpaStudentRepository.findAll(spec, pageable);
    }

    @Override
    public UUID save(StudentEntity student) {
        StudentEntity studentEntity = jpaStudentRepository.save(student);

        return studentEntity.getId();
    }
}

@Repository
interface JpaStudentRepository extends JpaRepository<StudentEntity, UUID>, JpaSpecificationExecutor<StudentEntity> {

    @Query(
            value = "SELECT s FROM StudentEntity s JOIN s.user WHERE s.schoolClass.name = ?1"
    )
    List<StudentEntity> findAllInSchoolClass(String schoolClass);

    @Query(
            value = "SELECT s FROM StudentEntity s " +
                    "JOIN FETCH s.user LEFT JOIN FETCH s.grades WHERE s.schoolClass.name = ?1"
    )
    List<StudentEntity> findAllInSchoolClassWithGrades(String schoolClass);

    @Query(
            value = "SELECT s.schoolClass.name FROM StudentEntity s JOIN s.user u WHERE u.email = ?1"
    )
    Optional<String> findStudentSchoolClassByEmail(String email);
}
