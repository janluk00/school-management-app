package com.janluk.schoolmanagementapp.student.service;

import com.janluk.schoolmanagementapp.common.criteria.CommonUserFilters;
import com.janluk.schoolmanagementapp.common.email.EmailService;
import com.janluk.schoolmanagementapp.common.exception.EmailAlreadyExistsException;
import com.janluk.schoolmanagementapp.common.model.StudentEntity;
import com.janluk.schoolmanagementapp.common.repository.port.StudentRepository;
import com.janluk.schoolmanagementapp.common.schema.StudentDTO;
import com.janluk.schoolmanagementapp.common.user.RoleAdder;
import com.janluk.schoolmanagementapp.common.user.UserValidator;
import com.janluk.schoolmanagementapp.student.criteria.StudentSearcher;
import com.janluk.schoolmanagementapp.student.mapper.StudentMapper;
import com.janluk.schoolmanagementapp.student.schema.CreateStudentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminStudentService {

    private final UserValidator userValidator;
    private final StudentMapper studentMapper;
    private final RoleAdder roleAdder;
    private final StudentRepository studentRepository;
    private final StudentSearcher studentSearcher;
    private final EmailService emailService;

    public Page<StudentDTO> searchStudents(CommonUserFilters userFilters, Pageable pageable) {
        Page<StudentEntity> students = studentSearcher.searchStudents(userFilters, pageable);

        return studentMapper.pageStudentEntitiesToPageStudentDTOs(students);
    }

    public StudentDTO getStudentById(UUID id) {
        return studentMapper.studentEntityToStudentDTO(studentRepository.getById(id));
    }

    @Transactional
    public String createStudent(CreateStudentRequest request) {
        if (!userValidator.isEmailUnique(request.user().email())) {
            log.warn("Email: %s already in use.".formatted(request.user().email()));
            throw new EmailAlreadyExistsException(request.user().email());
        }

        StudentEntity student = studentMapper.createStudentRequestToStudentEntity(request);
        String studentId = student.getId().toString();
        roleAdder.addStudentRole(student);

        studentRepository.save(student);

        emailService.sendNotification(student.getUser().getEmail(), student.getUser().getPasswordConfirmationToken());

        return studentId;
    }
}
