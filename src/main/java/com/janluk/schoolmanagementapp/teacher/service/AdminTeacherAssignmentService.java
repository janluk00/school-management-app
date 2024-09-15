package com.janluk.schoolmanagementapp.teacher.service;

import com.janluk.schoolmanagementapp.common.exception.TeacherNotTeachingSubjectException;
import com.janluk.schoolmanagementapp.common.model.SchoolClassEntity;
import com.janluk.schoolmanagementapp.common.model.SchoolSubjectEntity;
import com.janluk.schoolmanagementapp.common.model.TeacherEntity;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.common.repository.port.SchoolClassRepository;
import com.janluk.schoolmanagementapp.common.repository.port.SchoolSubjectRepository;
import com.janluk.schoolmanagementapp.common.repository.port.TeacherRepository;
import com.janluk.schoolmanagementapp.common.schema.SchoolClassRequest;
import com.janluk.schoolmanagementapp.common.schema.SchoolSubjectRequest;
import com.janluk.schoolmanagementapp.teacher.exception.TeacherAlreadyTeachingSubjectException;
import com.janluk.schoolmanagementapp.teacher.exception.TeacherIsAlreadyTutorException;
import com.janluk.schoolmanagementapp.teacher.exception.TeacherNotAssignedAsTutorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminTeacherAssignmentService {

    private final TeacherRepository teacherRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final SchoolSubjectRepository schoolSubjectRepository;

    @Transactional
    public String assignTutorToTeacher(UUID id, SchoolClassRequest request) {
        TeacherEntity teacher = teacherRepository.getById(id);
        SchoolClassEntity schoolClass = schoolClassRepository.getById(request.classType());

        if (isTeacherTutorOfSchoolClass(teacher, schoolClass)) {
            log.warn(
                    "Teacher with id: %s is already the tutor of class: %s."
                            .formatted(teacher.getId(), request.classType().name())
            );
            throw new TeacherIsAlreadyTutorException(teacher.getId().toString(), request.classType().name());
        }
        teacher.assignTutor(schoolClass);

        return teacher.getId().toString();
    }

    @Transactional
    public String removeTutorAssignment(UUID id) {
        TeacherEntity teacher = teacherRepository.getById(id);

        if (!isTeacherAlreadyTutor(teacher)) {
            log.warn(
                    "Teacher with id %s is not assigned as a tutor to any school class."
                            .formatted(teacher.getId().toString())
            );
            throw new TeacherNotAssignedAsTutorException(teacher.getId().toString());
        }

        teacher.removeTutoring();

        return teacher.getId().toString();
    }

    @Transactional
    public String assignSubjectToTutor(UUID id, SchoolSubjectRequest request) {
        TeacherEntity teacher = teacherRepository.getById(id);
        SchoolSubjectEntity schoolSubject = schoolSubjectRepository.getById(request.subjectType());

        if (isTeacherOfSchoolSubject(teacher, schoolSubject)) {
            log.warn(
                    "Teacher with id: %s is already teaching subject: %s."
                            .formatted(teacher.getId().toString(), request.subjectType().name())
            );
            throw new TeacherAlreadyTeachingSubjectException(teacher.getId().toString(), request.subjectType().name());
        }

        teacher.getTaughtSubjects().add(schoolSubject);

        return teacher.getId().toString();
    }

    @Transactional
    public String removeSubjectFromTeacher(UUID id, SubjectType subject) {
        TeacherEntity teacher = teacherRepository.getById(id);
        SchoolSubjectEntity schoolSubject = schoolSubjectRepository.getById(subject);

        if (!isTeacherOfSchoolSubject(teacher, schoolSubject)) {
            log.warn("Teacher with id: %s does not teach subject: %s.".formatted(teacher.getId(), subject.name()));
            throw new TeacherNotTeachingSubjectException(teacher.getId().toString(), subject.name());
        }

        teacher.getTaughtSubjects().remove(schoolSubject);

        return teacher.getId().toString();
    }

    private boolean isTeacherTutorOfSchoolClass(TeacherEntity teacher, SchoolClassEntity schoolClass) {
        SchoolClassEntity tutorClass = teacher.getTutorClass();

        return tutorClass != null && tutorClass.equals(schoolClass);
    }

    private boolean isTeacherAlreadyTutor(TeacherEntity teacher) {
        return teacher.getTutorClass() != null;
    }

    private boolean isTeacherOfSchoolSubject(TeacherEntity teacher, SchoolSubjectEntity schoolSubject) {
        Set<SchoolSubjectEntity> taughtSubjects = teacher.getTaughtSubjects();

        return taughtSubjects.contains(schoolSubject);
    }
}
