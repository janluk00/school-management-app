package com.janluk.schoolmanagementapp.schoolClass.service;

import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.repository.port.*;
import com.janluk.schoolmanagementapp.common.schema.StudentDTO;
import com.janluk.schoolmanagementapp.common.schema.TaughtSubjectInCourseDTO;
import com.janluk.schoolmanagementapp.schoolClass.mapper.SchoolClassMapper;
import com.janluk.schoolmanagementapp.schoolClass.schema.SchoolClassDTO;
import com.janluk.schoolmanagementapp.student.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminSchoolClassService {

    private final SchoolClassRepository schoolClassRepository;
    private final SchoolSubjectRepository schoolSubjectRepository;
    private final SchoolClassMapper schoolClassMapper;
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public Page<SchoolClassDTO> getAllSchoolClasses(Pageable pageable) {
        return schoolClassMapper.pageSchoolClassEntitiesToSchoolClassDTOs(schoolClassRepository.getAll(pageable));
    }

    public List<StudentDTO> getAllStudentsInClass(ClassType schoolClass) {
        return studentMapper.studentEntitiesToStudentDTOs(studentRepository.getAllInSchoolClass(schoolClass.name()));
    }

    public List<TaughtSubjectInCourseDTO> getAllCoursesForClass(ClassType schoolClass) {
        return schoolSubjectRepository.getAllTaughtSubjectsInCourse(schoolClass.name());
    }
}
