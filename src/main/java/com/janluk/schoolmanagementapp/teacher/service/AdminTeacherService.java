package com.janluk.schoolmanagementapp.teacher.service;

import com.janluk.schoolmanagementapp.common.exception.EmailAlreadyExistsException;
import com.janluk.schoolmanagementapp.common.model.SchoolClassEntity;
import com.janluk.schoolmanagementapp.common.model.TeacherEntity;
import com.janluk.schoolmanagementapp.common.repository.port.SchoolClassRepository;
import com.janluk.schoolmanagementapp.common.repository.port.TeacherRepository;
import com.janluk.schoolmanagementapp.common.schema.SchoolClassRequest;
import com.janluk.schoolmanagementapp.common.user.RoleAdder;
import com.janluk.schoolmanagementapp.common.user.UserValidator;
import com.janluk.schoolmanagementapp.teacher.exception.TeacherIsAlreadyTutor;
import com.janluk.schoolmanagementapp.teacher.exception.TeacherNotAssignedAsTutorException;
import com.janluk.schoolmanagementapp.teacher.mapper.TeacherMapper;
import com.janluk.schoolmanagementapp.teacher.schema.CreateTeacherRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminTeacherService {

    private final UserValidator userValidator;
    private final TeacherRepository teacherRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final TeacherMapper teacherMapper;
    private final RoleAdder roleAdder;

    @Transactional
    public String createTeacher(CreateTeacherRequest request) {
        if (!userValidator.isEmailUnique(request.user().email())) {
            log.warn("Email: %s already in use.".formatted(request.user().email()));
            throw new EmailAlreadyExistsException(request.user().email());
        }

        TeacherEntity teacher = teacherMapper.teacherCreateRequestToTeacherEntity(request, request.user());
        roleAdder.addTeacherRole(teacher);

        return teacherRepository.save(teacher).toString();
    }

    @Transactional
    public String assignTutorToTeacher(UUID id, SchoolClassRequest request) {
        TeacherEntity teacher = teacherRepository.getById(id);
        SchoolClassEntity schoolClass = schoolClassRepository.getById(request.classType().name());

        if (isTeacherTutorOfClass(teacher, schoolClass)) {
            log.warn(
                    "Teacher with id %s is already the tutor of class %s."
                            .formatted(teacher.getId(), request.classType().name())
            );
            throw new TeacherIsAlreadyTutor(teacher.getId().toString(), request.classType().name());
        }
        teacher.assignTutor(schoolClass);

        return teacher.getId().toString();
    }

    @Transactional
    public void removeTutorAssignment(UUID id) {
        TeacherEntity teacher = teacherRepository.getById(id);

        if (!isTeacherAlreadyTutor(teacher)) {
            log.warn(
                    "Teacher with id %s is not assigned as a tutor to any school class."
                            .formatted(teacher.getId().toString())
            );
            throw new TeacherNotAssignedAsTutorException(teacher.getId().toString());
        }

        teacher.setTutorClass(null);
    }

    private boolean isTeacherTutorOfClass(TeacherEntity teacher, SchoolClassEntity schoolClass) {
        SchoolClassEntity tutorClass = teacher.getTutorClass();

        return tutorClass != null && tutorClass.equals(schoolClass);
    }

    private boolean isTeacherAlreadyTutor(TeacherEntity teacher) {
        return teacher.getTutorClass() != null;
    }
}
