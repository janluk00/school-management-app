package com.janluk.schoolmanagementapp.subject.service;

import com.janluk.schoolmanagementapp.common.model.SchoolClassEntity;
import com.janluk.schoolmanagementapp.common.model.SchoolSubjectEntity;
import com.janluk.schoolmanagementapp.common.model.TeacherEntity;
import com.janluk.schoolmanagementapp.common.model.TeacherInCourseEntity;
import com.janluk.schoolmanagementapp.common.repository.port.SchoolClassRepository;
import com.janluk.schoolmanagementapp.common.repository.port.SchoolSubjectRepository;
import com.janluk.schoolmanagementapp.common.repository.port.TeacherInCourseRepository;
import com.janluk.schoolmanagementapp.common.repository.port.TeacherRepository;
import com.janluk.schoolmanagementapp.subject.exception.SchoolClassAlreadyHasTeacherOfSchoolSubjectException;
import com.janluk.schoolmanagementapp.subject.mapper.SchoolSubjectMapper;
import com.janluk.schoolmanagementapp.subject.schema.AssignTeacherToCourseRequest;
import com.janluk.schoolmanagementapp.subject.schema.SchoolSubjectDTO;
import com.janluk.schoolmanagementapp.teacher.exception.TeacherNotTeachingSubjectException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminSchoolSubjectService {

    private final SchoolSubjectRepository schoolSubjectRepository;
    private final SchoolSubjectMapper schoolSubjectMapper;
    private final SchoolClassRepository schoolClassRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherInCourseRepository teacherInCourseRepository;

    public Page<SchoolSubjectDTO> getAllSchoolSubjects(Pageable pageable) {
        return schoolSubjectMapper.pageSchoolSubjectEntitiesToSchoolSubjectDTOs(
                schoolSubjectRepository.getAll(pageable)
        );
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
        ).orElse( // new instance
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
}
