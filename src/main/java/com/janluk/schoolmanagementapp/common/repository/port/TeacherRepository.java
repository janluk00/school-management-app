package com.janluk.schoolmanagementapp.common.repository.port;

import com.janluk.schoolmanagementapp.common.model.TeacherEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface TeacherRepository {

    TeacherEntity getById(UUID id);

    Page<TeacherEntity> getAll(Specification<TeacherEntity> spec, Pageable pageable);

    UUID save (TeacherEntity teacher);
}
