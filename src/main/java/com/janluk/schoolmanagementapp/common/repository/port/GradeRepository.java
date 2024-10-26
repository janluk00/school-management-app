package com.janluk.schoolmanagementapp.common.repository.port;

import com.janluk.schoolmanagementapp.student.schema.GradeDTO;
import com.janluk.schoolmanagementapp.student.schema.SchoolSubjectGradePointAverageDTO;

import java.util.List;

public interface GradeRepository {

    List<GradeDTO> getAllGradesByStudent(String email);

    List<SchoolSubjectGradePointAverageDTO> getGradePointAveragesByStudent(String email);
}
