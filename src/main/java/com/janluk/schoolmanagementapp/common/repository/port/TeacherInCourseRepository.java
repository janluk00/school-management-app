package com.janluk.schoolmanagementapp.common.repository.port;

import com.janluk.schoolmanagementapp.common.model.TeacherInCourseEntity;

import java.util.Optional;
import java.util.UUID;

public interface TeacherInCourseRepository {

    Optional<TeacherInCourseEntity> getByTeacherAndSchoolSubject(UUID teacherId, String schoolSubject);

    UUID save(TeacherInCourseEntity teacherInCourse);
}
