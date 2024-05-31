package com.janluk.schoolmanagementapp.common.repository.adapter;

import com.janluk.schoolmanagementapp.common.model.TeacherEntity;
import com.janluk.schoolmanagementapp.common.repository.port.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SqlTeacherRepository implements TeacherRepository {

    private final JpaTeacherRepository jpaTeacherRepository;


    @Override
    public UUID save(TeacherEntity teacher) {
        TeacherEntity teacherEntity = jpaTeacherRepository.save(teacher);

        return teacher.getId();
    }
}

interface JpaTeacherRepository extends JpaRepository<TeacherEntity, UUID> {
}
