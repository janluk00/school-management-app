package com.janluk.schoolmanagementapp.common.repository.adapter;

import com.janluk.schoolmanagementapp.common.model.StudentEntity;
import com.janluk.schoolmanagementapp.common.repository.port.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SqlStudentRepository implements StudentRepository {

    private final JpaStudentRepository jpaStudentRepository;

    @Override
    public List<StudentEntity> getAllInSchoolClass(String schoolClass) {
        return jpaStudentRepository.findAllInSchoolClass(schoolClass);
    }
}

@Repository
interface JpaStudentRepository extends JpaRepository<StudentEntity, UUID> {

    @Query(
            value = "SELECT s.* FROM students AS s JOIN school_users AS sc ON s.school_user_id = sc.id " +
            "WHERE s.school_class_name = ?1",
            nativeQuery = true
    )
    List<StudentEntity> findAllInSchoolClass(String schoolClass);
}
