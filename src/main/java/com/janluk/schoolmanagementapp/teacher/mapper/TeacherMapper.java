package com.janluk.schoolmanagementapp.teacher.mapper;

import com.janluk.schoolmanagementapp.common.model.SchoolSubjectEntity;
import com.janluk.schoolmanagementapp.common.model.TeacherEntity;
import com.janluk.schoolmanagementapp.common.model.UserEntity;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.common.schema.CreateUserRequest;
import com.janluk.schoolmanagementapp.common.schema.UserBaseInformationDTO;
import com.janluk.schoolmanagementapp.common.user.UserCreator;
import com.janluk.schoolmanagementapp.teacher.schema.CreateTeacherRequest;
import com.janluk.schoolmanagementapp.teacher.schema.TeacherSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TeacherMapper {

    public TeacherEntity teacherCreateRequestToTeacherEntity(CreateTeacherRequest createRequest, CreateUserRequest user) {
        return TeacherEntity.builder()
                .id(UUID.randomUUID())
                .user(UserCreator.createUserEntity(user))
                .taughtSubjects(subjectTypesToSubjectEntities(createRequest.taughtSubjects()))
                .build();
    }

    public Page<TeacherSearchDTO> teacherEntitiesToPageTeacherSearchDTOs(Page<TeacherEntity> teachers) {
        return teachers.map(this::teacherEntityToTeacherSearchDTO);
    }

    private TeacherSearchDTO teacherEntityToTeacherSearchDTO(TeacherEntity teacher) {
        return TeacherSearchDTO.builder()
                .id(teacher.getId())
                .user(userEntityToUserBaseInformationDTO(teacher.getUser()))
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

    private Set<SchoolSubjectEntity> subjectTypesToSubjectEntities(Set<SubjectType> subjectTypes) {
        return subjectTypes.stream()
                .map(item -> SchoolSubjectEntity.builder()
                        .name(item.name())
                        .build())
                .collect(Collectors.toSet());
    }
}
