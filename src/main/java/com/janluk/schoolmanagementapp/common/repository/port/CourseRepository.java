package com.janluk.schoolmanagementapp.common.repository.port;

import com.janluk.schoolmanagementapp.common.model.CourseEntity;

import java.util.Optional;
import java.util.UUID;

public interface CourseRepository {

    Optional<CourseEntity> getByTeacherAndSchoolSubject(UUID teacherId, String schoolSubject);

    UUID save(CourseEntity course);
}
