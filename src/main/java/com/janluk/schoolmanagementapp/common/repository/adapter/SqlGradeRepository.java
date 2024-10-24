package com.janluk.schoolmanagementapp.common.repository.adapter;

import com.janluk.schoolmanagementapp.common.model.GradeEntity;
import com.janluk.schoolmanagementapp.common.repository.port.GradeRepository;
import com.janluk.schoolmanagementapp.student.schema.GradeDTO;
import com.janluk.schoolmanagementapp.student.schema.SchoolSubjectGradePointAverageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SqlGradeRepository implements GradeRepository {

    private final JpaGradeRepository jpaGradeRepository;

    @Override
    public List<GradeDTO> getAllGradesByStudent(String email) {
        return jpaGradeRepository.findAllGradesByStudent(email);
    }

    @Override
    public List<SchoolSubjectGradePointAverageDTO> getGradePointAveragesByStudent(String email) {
        return jpaGradeRepository.findGradePointAveragesByStudent(email);
    }
}

@Repository
interface JpaGradeRepository extends JpaRepository<GradeEntity, UUID> {

    @Query(
            value = "SELECT " +
                    "new com.janluk.schoolmanagementapp.student.schema.GradeDTO( " +
                    "g.subjectName, g.grade) " +
                    "FROM StudentEntity s " +
                    "JOIN s.grades g " +
                    "JOIN s.user u " +
                    "WHERE u.email = ?1"
    )
    List<GradeDTO> findAllGradesByStudent(String email);

    @Query(
            value = "SELECT " +
                    "new com.janluk.schoolmanagementapp.student.schema.SchoolSubjectGradePointAverageDTO( " +
                    "g.subjectName, avg(g.grade)) " +
                    "FROM StudentEntity s " +
                    "JOIN s.grades g " +
                    "JOIN s.user u " +
                    "WHERE u.email = ?1 " +
                    "GROUP BY g.subjectName"
    )
    List<SchoolSubjectGradePointAverageDTO> findGradePointAveragesByStudent(String email);
}
