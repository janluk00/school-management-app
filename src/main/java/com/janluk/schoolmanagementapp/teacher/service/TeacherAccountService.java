package com.janluk.schoolmanagementapp.teacher.service;

import com.janluk.schoolmanagementapp.common.mapper.GradeMapper;
import com.janluk.schoolmanagementapp.common.model.*;
import com.janluk.schoolmanagementapp.common.repository.port.SchoolClassRepository;
import com.janluk.schoolmanagementapp.common.repository.port.SchoolSubjectRepository;
import com.janluk.schoolmanagementapp.common.repository.port.StudentRepository;
import com.janluk.schoolmanagementapp.common.repository.port.TeacherRepository;
import com.janluk.schoolmanagementapp.common.schema.CourseDTO;
import com.janluk.schoolmanagementapp.student.mapper.StudentMapper;
import com.janluk.schoolmanagementapp.teacher.exception.TeacherNotInCourseException;
import com.janluk.schoolmanagementapp.teacher.schema.AddGradeRequest;
import com.janluk.schoolmanagementapp.teacher.schema.CreateGradeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherAccountService {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final SchoolSubjectRepository schoolSubjectRepository;
    private final GradeMapper gradeMapper;
    private final StudentMapper studentMapper;

    public List<CourseDTO> getAllCoursesByTeacher(String email) {
        return teacherRepository.getAllCoursesByTeacher(email);
    }

    @Transactional
    public CreateGradeResponse addGrade(UUID studentId, AddGradeRequest request, String email) {
        StudentEntity studentEntity = studentRepository.getById(studentId);
        SchoolSubjectEntity schoolSubject = schoolSubjectRepository.getById(request.subjectType());
        SchoolClassEntity schoolClass = studentEntity.getSchoolClass();
        TeacherEntity teacher = teacherRepository.getByEmail(email);

        if (!doesSchoolClassCourseInSchoolSubjectExists(teacher, studentEntity.getSchoolClass(), schoolSubject)) {
            log.warn("Teacher with email %s is not assigned to course with school subject %s and school class %s."
                    .formatted(email, schoolSubject.getName(), schoolClass.getName()));
            throw new TeacherNotInCourseException(email, schoolSubject.getName(), schoolClass.getName());
        }

        GradeEntity grade = gradeMapper.toGradeEntity(
                request.grade().value,
                studentId,
                schoolSubject.getName(),
                teacher.getId()
        );

        studentEntity.addGrade(grade);
        String gradeId = grade.getId().toString();

        return new CreateGradeResponse(gradeId);
    }

    private boolean doesSchoolClassCourseInSchoolSubjectExists(
            TeacherEntity teacher,
            SchoolClassEntity schoolClass,
            SchoolSubjectEntity schoolSubject
    ) {
        return teacher.getCourses().stream()
                .anyMatch(course -> course.getSubject().equals(schoolSubject) &&
                        course.getSchoolClasses().contains(schoolClass));
    }
}
