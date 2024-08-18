package com.janluk.schoolmanagementapp.teacher.service;

import com.janluk.schoolmanagementapp.common.criteria.CommonUserFilters;
import com.janluk.schoolmanagementapp.common.exception.EmailAlreadyExistsException;
import com.janluk.schoolmanagementapp.common.model.TeacherEntity;
import com.janluk.schoolmanagementapp.common.repository.port.TeacherRepository;
import com.janluk.schoolmanagementapp.common.schema.CourseDTO;
import com.janluk.schoolmanagementapp.common.user.RoleAdder;
import com.janluk.schoolmanagementapp.common.user.UserValidator;
import com.janluk.schoolmanagementapp.teacher.criteria.TeacherSearcher;
import com.janluk.schoolmanagementapp.teacher.mapper.TeacherMapper;
import com.janluk.schoolmanagementapp.teacher.schema.CreateTeacherRequest;
import com.janluk.schoolmanagementapp.teacher.schema.TeacherDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminTeacherService {

    private final UserValidator userValidator;
    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final RoleAdder roleAdder;
    private final TeacherSearcher teacherSearcher;

    public Page<TeacherDTO> searchTeachers(CommonUserFilters userFilters, Pageable pageable) {
        Page<TeacherEntity> teachers = teacherSearcher.searchTeachers(userFilters, pageable);

        return teacherMapper.pageTeacherEntitiesToPageTeacherDTOs(teachers);
    }

    public TeacherDTO getTeacherById(UUID id) {
        return teacherMapper.teacherEntityToTeacherDTO(teacherRepository.getById(id));
    }

    public List<CourseDTO> getAllCoursesByTeacherId(UUID id) {
        return teacherRepository.getAllCoursesByTeacher(id);
    }

    @Transactional
    public String createTeacher(CreateTeacherRequest request) {
        if (!userValidator.isEmailUnique(request.user().email())) {
            log.warn("Email: %s already in use.".formatted(request.user().email()));
            throw new EmailAlreadyExistsException(request.user().email());
        }

        TeacherEntity teacher = teacherMapper.createTeacherRequestToTeacherEntity(request);
        roleAdder.addTeacherRole(teacher);

        return teacherRepository.save(teacher).toString();
    }
}
