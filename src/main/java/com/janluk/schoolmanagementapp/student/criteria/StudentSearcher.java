package com.janluk.schoolmanagementapp.student.criteria;

import com.janluk.schoolmanagementapp.common.criteria.CommonUserFilters;
import com.janluk.schoolmanagementapp.common.model.StudentEntity;
import com.janluk.schoolmanagementapp.common.repository.port.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentSearcher {

    private final StudentRepository studentRepository;

    public Page<StudentEntity> searchStudents(CommonUserFilters userFilters, Pageable pageable) {
        Specification<StudentEntity> specification = StudentSpecifications.withStudentFilters(userFilters);

        return studentRepository.getAll(specification, pageable);
    }
}
