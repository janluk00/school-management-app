package com.janluk.schoolmanagementapp.common.repository.port;

import com.janluk.schoolmanagementapp.common.model.TeacherEntity;
import com.janluk.schoolmanagementapp.common.schema.CourseDTO;
import com.janluk.schoolmanagementapp.teacher.schema.StudentPerformanceReportDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public interface TeacherRepository {

    TeacherEntity getById(UUID id);

    TeacherEntity getByEmail(String email);

    List<CourseDTO> getAllCoursesByTeacher(UUID id);

    List<CourseDTO> getAllCoursesByTeacher(String email);

    List<StudentPerformanceReportDTO> getStudentsPerformanceReportByTeacher(String email);

    Page<TeacherEntity> getAll(Specification<TeacherEntity> spec, Pageable pageable);

    UUID save(TeacherEntity teacher);
}
