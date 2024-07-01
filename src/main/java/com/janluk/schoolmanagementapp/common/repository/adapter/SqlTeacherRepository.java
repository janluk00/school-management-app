package com.janluk.schoolmanagementapp.common.repository.adapter;

import com.janluk.schoolmanagementapp.common.exception.NoResultFoundException;
import com.janluk.schoolmanagementapp.common.model.TeacherEntity;
import com.janluk.schoolmanagementapp.common.repository.port.TeacherRepository;
import com.janluk.schoolmanagementapp.common.schema.CourseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SqlTeacherRepository implements TeacherRepository {

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
    public List<CourseDTO> getAllCoursesByTeacher(UUID id) {
        return jpaTeacherRepository.findAllCoursesByTeacher(id);
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

interface JpaTeacherRepository extends JpaRepository<TeacherEntity, UUID>, JpaSpecificationExecutor<TeacherEntity> {

    @Query(
            value = "SELECT " +
                    "new com.janluk.schoolmanagementapp.common.schema.CourseDTO(sc.name, tic.subject.name) " +
                    "FROM TeacherEntity t JOIN t.teacherInCourses tic JOIN tic.schoolClasses sc WHERE t.id = ?1"
    )
    List<CourseDTO> findAllCoursesByTeacher(UUID id);
}
