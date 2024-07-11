package com.janluk.schoolmanagementapp.common.repository.port;

import com.janluk.schoolmanagementapp.common.model.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public interface StudentRepository {

    StudentEntity getById(UUID id);

    List<StudentEntity> getAllInSchoolClass(String schoolClass);

    List<StudentEntity> getAllInSchoolClassWithGrades(String schoolClass);

    Page<StudentEntity> getAll(Specification<StudentEntity> spec, Pageable pageable);

    UUID save(StudentEntity student);
}
