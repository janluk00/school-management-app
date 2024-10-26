package com.janluk.schoolmanagementapp.common.repository.port;

import com.janluk.schoolmanagementapp.common.model.UserEntity;

import java.util.UUID;

public interface UserRepository {

    UserEntity getByEmail(String email);

    UserEntity getByEmailWithRoles(String email);

    UserEntity getByPasswordConfirmationToken(String passwordConfirmationToken);

    boolean existsByEmail(String email);

    UUID save(UserEntity user);
}
