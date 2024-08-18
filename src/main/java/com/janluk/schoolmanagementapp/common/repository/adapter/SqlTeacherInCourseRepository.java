package com.janluk.schoolmanagementapp.common.repository.adapter;

import com.janluk.schoolmanagementapp.common.model.TeacherInCourseEntity;
import com.janluk.schoolmanagementapp.common.repository.port.TeacherInCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
class SqlTeacherInCourseRepository implements TeacherInCourseRepository {

    private final JpaTeacherInCourseRepository jpaTeacherInCourseRepository;

    @Override
    public Optional<TeacherInCourseEntity> getByTeacherAndSchoolSubject(UUID teacherId, String schoolSubject) {
        return jpaTeacherInCourseRepository.findByTeacherIdAndSchoolSubject(teacherId, schoolSubject);
    }

    @Override
    public UUID save(TeacherInCourseEntity teacherInCourse) {
        TeacherInCourseEntity teacher = jpaTeacherInCourseRepository.save(teacherInCourse);

        return teacher.getId();
    }
}

@Repository
interface JpaTeacherInCourseRepository extends JpaRepository<TeacherInCourseEntity, UUID> {

    @Query(
            value = "SELECT tic.* FROM teachers_in_course AS tic " +
                    "WHERE tic.teacher_id = ?1 AND tic.subject_name = ?2",
            nativeQuery = true
    )
    Optional<TeacherInCourseEntity> findByTeacherIdAndSchoolSubject(UUID teacherId, String schoolSubject);
}
