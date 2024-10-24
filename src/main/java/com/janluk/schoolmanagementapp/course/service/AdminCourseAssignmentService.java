package com.janluk.schoolmanagementapp.course.service;

import com.janluk.schoolmanagementapp.common.exception.TeacherNotTeachingSubjectException;
import com.janluk.schoolmanagementapp.common.model.SchoolClassEntity;
import com.janluk.schoolmanagementapp.common.model.SchoolSubjectEntity;
import com.janluk.schoolmanagementapp.common.model.TeacherEntity;
import com.janluk.schoolmanagementapp.common.model.CourseEntity;
import com.janluk.schoolmanagementapp.common.repository.port.*;
import com.janluk.schoolmanagementapp.course.exception.SchoolClassAlreadyHasTeacherOfSchoolSubjectException;
import com.janluk.schoolmanagementapp.course.exception.CourseNotAssignedToSchoolClassException;
import com.janluk.schoolmanagementapp.course.schema.AssignTeacherToCourseRequest;
import com.janluk.schoolmanagementapp.course.schema.AssignTeacherToCourseResponse;
import com.janluk.schoolmanagementapp.course.schema.RemoveTeacherFromCourseRequest;
import com.janluk.schoolmanagementapp.course.schema.RemoveTeacherFromCourseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminCourseAssignmentService {

    private final SchoolClassRepository schoolClassRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final SchoolSubjectRepository schoolSubjectRepository;

    @Transactional
    public AssignTeacherToCourseResponse assignTeacherToCourse(AssignTeacherToCourseRequest request) {
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
                    "School class with name: %s already has a teacher of school subject with name: %s."
                            .formatted(schoolClass, schoolSubject)
            );
            throw new SchoolClassAlreadyHasTeacherOfSchoolSubjectException(
                    request.classType().name(),
                    request.subjectType().name()
            );
        }

        UUID courseId = assignTeacherToCourse(teacher, schoolClass, schoolSubject);

        return new AssignTeacherToCourseResponse(courseId.toString());
    }

    @Transactional
    public RemoveTeacherFromCourseResponse removeTeacherFromCourse(RemoveTeacherFromCourseRequest request) {
        TeacherEntity teacher = teacherRepository.getById(request.teacherId());
        SchoolClassEntity schoolClass = schoolClassRepository.getById(request.classType());

        CourseEntity course = getTeacherOfSubject(teacher, request.subjectType().name());

        if (!isSchoolClassAssignedToCourse(course, schoolClass)) {
            log.warn(
                    "School class: %s is not assigned to course with id: %s"
                            .formatted(request.classType().name(), course.getId().toString())
            );
            throw new CourseNotAssignedToSchoolClassException(
                    course.getId().toString(),
                    request.classType().name()
            );
        }

        course.removeFromClass(schoolClass);
        String courseId = course.getId().toString();

        return new RemoveTeacherFromCourseResponse(courseId);
    }

    private boolean isTeacherOfSchoolSubject(TeacherEntity teacher, SchoolSubjectEntity schoolSubject) {
        Set<SchoolSubjectEntity> taughtSubjects = teacher.getTaughtSubjects();

        return taughtSubjects.contains(schoolSubject);
    }

    private boolean doesSchoolClassAlreadyHasTeacherOfSchoolSubject(
            SchoolClassEntity schoolClass, SchoolSubjectEntity schoolSubject
    ) {
        return schoolClass.getCourses().stream()
                .anyMatch(teacher -> teacher.getSubject().equals(schoolSubject));
    }

    private boolean hasCourseJustBeenCreated(CourseEntity course) {
        return course.getSchoolClasses().size() == 1;
    }

    private UUID assignTeacherToCourse(
            TeacherEntity teacher,
            SchoolClassEntity schoolClass,
            SchoolSubjectEntity schoolSubject
    ) {
        CourseEntity course = courseRepository.getByTeacherAndSchoolSubject(
                teacher.getId(),
                schoolSubject.getName()
        ).orElse(
                CourseEntity.builder()
                        .build()
        );

        course.assignToClass(schoolClass);

        if (hasCourseJustBeenCreated(course)) {
            course.assignTeacher(teacher);
            course.assignSubject(schoolSubject);
        }

        return courseRepository.save(course);
    }

    private CourseEntity getTeacherOfSubject(TeacherEntity teacher, String schoolSubject) {
        return teacher.getCourses().stream()
                .filter(course -> course.getSubject().getName().equals(schoolSubject))
                .findFirst()
                .orElseThrow(
                        () -> new TeacherNotTeachingSubjectException(teacher.getId().toString(), schoolSubject)
                );
    }

    private boolean isSchoolClassAssignedToCourse(
            CourseEntity course,
            SchoolClassEntity schoolClass
    ) {
        return course.getSchoolClasses().stream()
                .anyMatch(taughtSchoolClass -> taughtSchoolClass.getName().equals(schoolClass.getName()));
    }
}
