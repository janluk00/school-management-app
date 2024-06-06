package com.janluk.schoolmanagementapp.teacher.criteria;

import com.janluk.schoolmanagementapp.common.criteria.CommonUserFilters;
import com.janluk.schoolmanagementapp.common.model.TeacherEntity;
import com.janluk.schoolmanagementapp.common.repository.port.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeacherSearcher {

    private final TeacherRepository teacherRepository;

    public Page<TeacherEntity> searchTeachers(CommonUserFilters userFilters, Pageable pageable) {
        Specification<TeacherEntity> specification = TeacherSpecifications.withTeacherFilters(userFilters);

        return teacherRepository.getAll(specification, pageable);
    }
}
