package com.janluk.schoolmanagementapp.teacher.service;

import com.janluk.schoolmanagementapp.common.mapper.GradeMapper;
import com.janluk.schoolmanagementapp.common.model.*;
import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.common.repository.port.SchoolClassRepository;
import com.janluk.schoolmanagementapp.common.repository.port.SchoolSubjectRepository;
import com.janluk.schoolmanagementapp.common.repository.port.StudentRepository;
import com.janluk.schoolmanagementapp.common.repository.port.TeacherRepository;
import com.janluk.schoolmanagementapp.common.schema.CourseDTO;
import com.janluk.schoolmanagementapp.student.mapper.StudentMapper;
import com.janluk.schoolmanagementapp.teacher.exception.TeacherNotInCourseException;
import com.janluk.schoolmanagementapp.teacher.schema.AddGradeRequest;
import com.janluk.schoolmanagementapp.teacher.schema.StudentPerformanceDTO;
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

    public List<StudentPerformanceDTO> getAllStudentsInCourse(
            ClassType schoolClass,
            SubjectType schoolSubject,
            String email
    ) {
        SchoolClassEntity taughtClass = schoolClassRepository.getById(schoolClass);
        TeacherEntity teacher = teacherRepository.getByEmail(email);
        SchoolSubjectEntity taughtSubject = schoolSubjectRepository.getById(schoolSubject);

        if (!isTeacherInCourseWithSubjectAndClass(teacher, taughtClass, taughtSubject)) {
            log.warn("Teacher with email %s is not assigned to course with school subject %s and school class %s."
                    .formatted(email, taughtSubject.getName(), taughtClass.getName()));
            throw new TeacherNotInCourseException(email, taughtSubject.getName(), taughtClass.getName());
        }

        return studentMapper.studentEntitiesToStudentPerformanceDTOs(
                studentRepository.getAllInSchoolClassWithGrades(taughtClass.getName())
        );
    }

    @Transactional
    public String addGrade(UUID studentId, AddGradeRequest request, String email) {
        StudentEntity studentEntity = studentRepository.getById(studentId);
        SchoolSubjectEntity schoolSubject = schoolSubjectRepository.getById(request.subjectType());
        SchoolClassEntity schoolClass = studentEntity.getSchoolClass();
        TeacherEntity teacher = teacherRepository.getByEmail(email);

        if (!isTeacherInCourseWithSubjectAndClass(teacher, studentEntity.getSchoolClass(), schoolSubject)) {
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

        return grade.getId().toString();
    }

    private boolean isTeacherInCourseWithSubjectAndClass(
            TeacherEntity teacher,
            SchoolClassEntity schoolClass,
            SchoolSubjectEntity schoolSubject
    ) {
        return teacher.getTeacherInCourses().stream()
                .anyMatch(teacherInCourse -> teacherInCourse.getSubject().equals(schoolSubject) &&
                        teacherInCourse.getSchoolClasses().contains(schoolClass));
    }
}
