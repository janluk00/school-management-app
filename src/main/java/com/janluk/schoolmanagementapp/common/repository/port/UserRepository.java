package com.janluk.schoolmanagementapp.common.repository.port;

import com.janluk.schoolmanagementapp.common.model.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<UserEntity> getByEmail(String email);

    UUID save(UserEntity user);
}
