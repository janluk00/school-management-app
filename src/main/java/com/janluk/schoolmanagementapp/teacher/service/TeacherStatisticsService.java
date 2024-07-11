package com.janluk.schoolmanagementapp.teacher.service;

import com.janluk.schoolmanagementapp.common.repository.port.TeacherRepository;
import com.janluk.schoolmanagementapp.teacher.schema.StudentPerformanceReportDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherStatisticsService {

    private final TeacherRepository teacherRepository;

    public List<StudentPerformanceReportDTO> getStudentsPerformanceReportByTeacher(String email) {
        return teacherRepository.getStudentsPerformanceReportByTeacher(email);
    }
}
