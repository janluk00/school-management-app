package com.janluk.schoolmanagementapp.student.service;

import com.janluk.schoolmanagementapp.common.repository.port.GradeRepository;
import com.janluk.schoolmanagementapp.common.repository.port.SchoolSubjectRepository;
import com.janluk.schoolmanagementapp.common.repository.port.StudentRepository;
import com.janluk.schoolmanagementapp.common.schema.TaughtSubjectInCourseDTO;
import com.janluk.schoolmanagementapp.student.mapper.StudentMapper;
import com.janluk.schoolmanagementapp.student.schema.GradeDTO;
import com.janluk.schoolmanagementapp.student.schema.SchoolSubjectGradePointAverageDTO;
import com.janluk.schoolmanagementapp.student.schema.SchoolSubjectGradesDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentAccountService {

    private final GradeRepository gradeRepository;
    private final StudentMapper studentMapper;
    private final SchoolSubjectRepository schoolSubjectRepository;
    private final StudentRepository studentRepository;

    public List<SchoolSubjectGradesDTO> getGradesGroupedBySchoolSubject(String email) {
        List<GradeDTO> studentGrades = gradeRepository.getAllGradesByStudent(email);

        Map<String, List<GradeDTO>> groupedGrades = studentGrades.stream()
                .collect(Collectors.groupingBy(GradeDTO::schoolSubject));

        return studentMapper.mapToSchoolSubjectGradesDTOs(groupedGrades);
    }

    public List<SchoolSubjectGradePointAverageDTO> getGradePointAveragesGroupedBySchoolSubject(String email) {
        return gradeRepository.getGradePointAveragesByStudent(email);
    }

    public List<TaughtSubjectInCourseDTO> getAllCoursesByStudent(String email) {
        String studentSchoolClass = studentRepository.getStudentSchoolClassByEmail(email);

        return schoolSubjectRepository.getAllTaughtSubjectsInCourse(studentSchoolClass);
    }
}
