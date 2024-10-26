package com.janluk.schoolmanagementapp.common.repository.adapter;

import com.janluk.schoolmanagementapp.common.model.CourseEntity;
import com.janluk.schoolmanagementapp.common.repository.port.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SqlCourseRepository implements CourseRepository {

    private final JpaCourseRepository jpaCourseRepository;

    @Override
    public Optional<CourseEntity> getByTeacherAndSchoolSubject(UUID teacherId, String schoolSubject) {
        return jpaCourseRepository.findByTeacherIdAndSchoolSubject(teacherId, schoolSubject);
    }

    @Override
    public UUID save(CourseEntity course) {
        CourseEntity teacher = jpaCourseRepository.save(course);

        return teacher.getId();
    }
}

@Repository
interface JpaCourseRepository extends JpaRepository<CourseEntity, UUID> {

    @Query(
            value = "SELECT c.* FROM courses AS c " +
                    "WHERE c.teacher_id = ?1 AND c.subject_name = ?2",
            nativeQuery = true
    )
    Optional<CourseEntity> findByTeacherIdAndSchoolSubject(UUID teacherId, String schoolSubject);
}
