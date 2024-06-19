package com.janluk.schoolmanagementapp.schoolClass.service;

import com.janluk.schoolmanagementapp.common.exception.TeacherNotTeachingSubjectException;
import com.janluk.schoolmanagementapp.common.model.SchoolClassEntity;
import com.janluk.schoolmanagementapp.common.model.SchoolSubjectEntity;
import com.janluk.schoolmanagementapp.common.model.TeacherEntity;
import com.janluk.schoolmanagementapp.common.model.TeacherInCourseEntity;
import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.repository.port.*;
import com.janluk.schoolmanagementapp.common.schema.StudentDTO;
import com.janluk.schoolmanagementapp.common.schema.TaughtSubjectInCourseDTO;
import com.janluk.schoolmanagementapp.schoolClass.exception.SchoolClassAlreadyHasTeacherOfSchoolSubjectException;
import com.janluk.schoolmanagementapp.schoolClass.exception.TeacherInCourseNotAssignedToSchoolClassException;
import com.janluk.schoolmanagementapp.schoolClass.mapper.SchoolClassMapper;
import com.janluk.schoolmanagementapp.schoolClass.schema.AssignTeacherToCourseRequest;
import com.janluk.schoolmanagementapp.schoolClass.schema.RemoveTeacherFromCourseRequest;
import com.janluk.schoolmanagementapp.schoolClass.schema.SchoolClassDTO;
import com.janluk.schoolmanagementapp.student.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminSchoolClassService {

    private final SchoolClassRepository schoolClassRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherInCourseRepository teacherInCourseRepository;
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
        return schoolSubjectRepository.getAllTaughtSubjectsInClass(schoolClass.name());
    }

    @Transactional
    public String assignTeacherToCourse(AssignTeacherToCourseRequest request) {
        TeacherEntity teacher = teacherRepository.getById(request.teacherId());
        SchoolClassEntity schoolClass = schoolClassRepository.getById(request.classType());
        SchoolSubjectEntity schoolSubject = schoolSubjectRepository.getById(request.subjectType());

        if (!isTeacherOfSchoolSubject(teacher, schoolSubject)) {
            log.warn(
                    "Teacher with id: %s is not teaching subject: %s."
                            .formatted(teacher.getId(), request.subjectType().name())
            );
            throw new TeacherNotTeachingSubjectException(teacher.getId().toString(), request.subjectType().name());
        }

        if (doesSchoolClassAlreadyHasTeacherOfSchoolSubject(schoolClass, schoolSubject)) {
            log.warn(
                    "School class with name: %s already has a teacher of school subject with name: %s"
                            .formatted(schoolClass, schoolSubject)
            );
            throw new SchoolClassAlreadyHasTeacherOfSchoolSubjectException(
                    request.classType().name(),
                    request.subjectType().name()
            );
        }

        UUID teacherInClassId = assignTeacherToCourse(teacher, schoolClass, schoolSubject);

        return teacherInClassId.toString();
    }

    @Transactional
    public void removeTeacherFromCourse(RemoveTeacherFromCourseRequest request) {
        TeacherEntity teacher = teacherRepository.getById(request.teacherId());
        SchoolClassEntity schoolClass = schoolClassRepository.getById(request.classType());

        TeacherInCourseEntity teacherInCourse = getTeacherOfSubject(teacher, request.subjectType().name());

        if (!doesTeacherInCourseOfSchoolClass(teacherInCourse, schoolClass)) {
            log.warn(
                    "Teacher in course with id: %s does not teach school class: %s"
                            .formatted(teacherInCourse.getId().toString(), request.classType().name())
            );
            throw new TeacherInCourseNotAssignedToSchoolClassException(
                    teacherInCourse.getId().toString(),
                    request.classType().name()
            );
        }

        teacherInCourse.removeFromClass(schoolClass);
    }

    private boolean isTeacherOfSchoolSubject(TeacherEntity teacher, SchoolSubjectEntity schoolSubject) {
        Set<SchoolSubjectEntity> taughtSubjects = teacher.getTaughtSubjects();

        return taughtSubjects.contains(schoolSubject);
    }

    private boolean doesSchoolClassAlreadyHasTeacherOfSchoolSubject(
            SchoolClassEntity schoolClass, SchoolSubjectEntity schoolSubject
    ) {
        return schoolClass.getTeachers().stream()
                .anyMatch(teacher -> teacher.getSubject().equals(schoolSubject));
    }

    private boolean hasTeacherInCourseJustBeenCreated(TeacherInCourseEntity teacherInCourse) {
        return teacherInCourse.getSchoolClasses().size() == 1;
    }

    private UUID assignTeacherToCourse(
            TeacherEntity teacher,
            SchoolClassEntity schoolClass,
            SchoolSubjectEntity schoolSubject
    ) {
        TeacherInCourseEntity teacherInCourse = teacherInCourseRepository.getByTeacherAndSchoolSubject(
                teacher.getId(),
                schoolSubject.getName()
        ).orElse(
                TeacherInCourseEntity.builder()
                        .id(UUID.randomUUID())
                        .build()
        );

        teacherInCourse.assignToClass(schoolClass);

        if (hasTeacherInCourseJustBeenCreated(teacherInCourse)) {
            teacherInCourse.setTeacher(teacher);
            teacherInCourse.setSubject(schoolSubject);
        }

        return teacherInCourseRepository.save(teacherInCourse);
    }

    private TeacherInCourseEntity getTeacherOfSubject(TeacherEntity teacher, String schoolSubject) {
        return teacher.getTeacherInCourses().stream()
                .filter(teacherInCourse -> teacherInCourse.getSubject().getName().equals(schoolSubject))
                .findFirst()
                .orElseThrow(
                        () -> new TeacherNotTeachingSubjectException(teacher.getId().toString(), schoolSubject)
                );
    }

    private boolean doesTeacherInCourseOfSchoolClass(
            TeacherInCourseEntity teacherInCourse,
            SchoolClassEntity schoolClass
    ) {
        return teacherInCourse.getSchoolClasses().stream()
                .anyMatch(taughtSchoolClass -> taughtSchoolClass.getName().equals(schoolClass.getName()));
    }
}
