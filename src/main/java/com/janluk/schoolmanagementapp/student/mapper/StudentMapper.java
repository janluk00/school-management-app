package com.janluk.schoolmanagementapp.student.mapper;

import com.janluk.schoolmanagementapp.common.model.StudentEntity;
import com.janluk.schoolmanagementapp.common.model.UserEntity;
import com.janluk.schoolmanagementapp.common.schema.StudentDTO;
import com.janluk.schoolmanagementapp.common.schema.UserBaseInformationDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentMapper {

    public List<StudentDTO> studentEntitiesToStudentDTOs(List<StudentEntity> students) {
        return students.stream()
                .map(this::studentEntityToStudentDTO)
                .toList();
    }

    private StudentDTO studentEntityToStudentDTO(StudentEntity student) {
        return StudentDTO.builder()
                .id(student.getId())
                .user(userEntityToUserBaseInformationDTO(student.getUser()))
                .build();
    }

    private UserBaseInformationDTO userEntityToUserBaseInformationDTO(UserEntity user) {
        return UserBaseInformationDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .birthDate(user.getBirthDate())
                .build();
    }
}
