package com.janluk.schoolmanagementapp.common.repository.adapter;

import com.janluk.schoolmanagementapp.common.exception.NoResultFoundException;
import com.janluk.schoolmanagementapp.common.model.TeacherEntity;
import com.janluk.schoolmanagementapp.common.repository.port.TeacherRepository;
import com.janluk.schoolmanagementapp.common.schema.CourseDTO;
import com.janluk.schoolmanagementapp.teacher.schema.StudentPerformanceReportDTO;
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
class SqlTeacherRepository implements TeacherRepository {

    private final JpaTeacherRepository jpaTeacherRepository;

    @Override
    public TeacherEntity getById(UUID id) {
        return jpaTeacherRepository.findById(id)
                .orElseThrow(() -> new NoResultFoundException(
                        "Could not find teacher with id: %s".formatted(id.toString())
                    )
                );
    }

    @Override
    public TeacherEntity getByEmail(String email) {
        return jpaTeacherRepository.findByEmail(email)
                .orElseThrow(() -> new NoResultFoundException(
                        "Could not find teacher with email: %s".formatted(email)
                    )
                );
    }

    @Override
    public List<CourseDTO> getAllCoursesByTeacher(UUID id) {
        return jpaTeacherRepository.findAllCoursesByTeacher(id);
    }

    @Override
    public List<CourseDTO> getAllCoursesByTeacher(String email) {
        return jpaTeacherRepository.findAllCoursesByTeacher(email);
    }

    @Override
    public List<StudentPerformanceReportDTO> getStudentsPerformanceReportByTeacher(String email) {
        return jpaTeacherRepository.findStatisticsOfStudentsByTeacher(email);
    }

    @Override
    public Page<TeacherEntity> getAll(Specification<TeacherEntity> spec, Pageable pageable) {
        return jpaTeacherRepository.findAll(spec, pageable);
    }

    @Override
    public UUID save(TeacherEntity teacher) {
        TeacherEntity teacherEntity = jpaTeacherRepository.save(teacher);

        return teacherEntity.getId();
    }
}

@Repository
interface JpaTeacherRepository extends JpaRepository<TeacherEntity, UUID>, JpaSpecificationExecutor<TeacherEntity> {

    @Query(
            value = "SELECT t FROM TeacherEntity t WHERE t.user.email = ?1"
    )
    Optional<TeacherEntity> findByEmail(String email);

    @Query(
            value = "SELECT " +
                    "new com.janluk.schoolmanagementapp.common.schema.CourseDTO(sc.name, c.subject.name) " +
                    "FROM TeacherEntity t JOIN t.courses c JOIN c.schoolClasses sc WHERE t.id = ?1"
    )
    List<CourseDTO> findAllCoursesByTeacher(UUID id);

    @Query(
            value = "SELECT " +
                    "new com.janluk.schoolmanagementapp.common.schema.CourseDTO(sc.name, c.subject.name) " +
                    "FROM TeacherEntity t JOIN t.courses c JOIN c.schoolClasses sc WHERE t.user.email = ?1"
    )
    List<CourseDTO> findAllCoursesByTeacher(String email);

    @Query(
            value = "SELECT " +
                    "new com.janluk.schoolmanagementapp.teacher.schema.StudentPerformanceReportDTO( " +
                    "s.schoolClass.name, c.subject.name, avg(g.grade)) " +
                    "FROM TeacherEntity t " +
                    "JOIN t.user u " +
                    "JOIN t.courses c " +
                    "JOIN c.schoolClasses sc " +
                    "JOIN sc.students s " +
                    "JOIN s.grades g " +
                    "WHERE u.email = ?1 " +
                    "GROUP BY s.schoolClass.name, c.subject.name"
    )
    List<StudentPerformanceReportDTO> findStatisticsOfStudentsByTeacher(String email);
}
