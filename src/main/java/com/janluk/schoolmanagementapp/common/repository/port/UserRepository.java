package com.janluk.schoolmanagementapp.common.repository.port;

import com.janluk.schoolmanagementapp.common.model.UserEntity;

import java.util.UUID;

public interface UserRepository {

    UserEntity getByEmail(String email);

    UserEntity getByPasswordConfirmationToken(String passwordConfirmationToken);

    UUID save(UserEntity user);

    boolean existsByEmail(String email);
}
