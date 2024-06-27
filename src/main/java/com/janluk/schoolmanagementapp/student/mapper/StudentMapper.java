package com.janluk.schoolmanagementapp.student.mapper;

import com.janluk.schoolmanagementapp.common.model.SchoolClassEntity;
import com.janluk.schoolmanagementapp.common.model.StudentEntity;
import com.janluk.schoolmanagementapp.common.model.UserEntity;
import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.schema.StudentDTO;
import com.janluk.schoolmanagementapp.common.schema.UserBaseInformationDTO;
import com.janluk.schoolmanagementapp.common.user.UserCreator;
import com.janluk.schoolmanagementapp.student.schema.CreateStudentRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class StudentMapper {

    public StudentEntity createStudentRequestToStudentEntity(CreateStudentRequest createRequest) {
        return StudentEntity.builder()
                .id(UUID.randomUUID())
                .user(UserCreator.createUserEntity(createRequest.user()))
                .schoolClass(classTypeToSchoolClassEntity(createRequest.schoolClass()))
                .build();
    }

    public List<StudentDTO> studentEntitiesToStudentDTOs(List<StudentEntity> students) {
        return students.stream()
                .map(this::studentEntityToStudentDTO)
                .toList();
    }

    public Page<StudentDTO> pageStudentEntitiesToPageStudentDTOs(Page<StudentEntity> students) {
        return students.map(this::studentEntityToStudentDTO);
    }

    public StudentDTO studentEntityToStudentDTO(StudentEntity student) {
        return StudentDTO.builder()
                .id(student.getId())
                .user(userEntityToUserBaseInformationDTO(student.getUser()))
                .schoolClass(student.getSchoolClass().getName())
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

    private SchoolClassEntity classTypeToSchoolClassEntity(ClassType classType) {
        return SchoolClassEntity.builder()
                .name(classType.name())
                .build();
    }
}
