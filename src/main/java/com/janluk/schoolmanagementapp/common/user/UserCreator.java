package com.janluk.schoolmanagementapp.common.user;

import com.janluk.schoolmanagementapp.common.model.UserEntity;
import com.janluk.schoolmanagementapp.common.model.vo.BirthDate;
import com.janluk.schoolmanagementapp.common.schema.CreateUserRequest;

import java.util.UUID;

import static com.janluk.schoolmanagementapp.common.user.TokenGenerator.generateToken;

public class UserCreator {

    public static UserEntity createUserEntity(
            String name,
            String surname,
            String email,
            BirthDate birthDate
            ) {
        return UserEntity.builder()
                .id(UUID.randomUUID())
                .name(name)
                .surname(surname)
                .email(email)
                .password(null)
                .birthDate(birthDate)
                .passwordConfirmationToken(generateToken())
                .build();
    }

    public static UserEntity createUserEntity(CreateUserRequest request) {
        return UserEntity.builder()
                .id(UUID.randomUUID())
                .name(request.name())
                .surname(request.surname())
                .email(request.email())
                .password(null)
                .birthDate(new BirthDate(request.birthDate()))
                .passwordConfirmationToken(generateToken())
                .build();
    }

}
