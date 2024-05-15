package com.janluk.schoolmanagementapp.common.repository.adapter;

import com.janluk.schoolmanagementapp.common.model.UserEntity;

import com.janluk.schoolmanagementapp.common.repository.port.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class SqlUserRepository implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    public SqlUserRepository(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public Optional<UserEntity> getByEmail(String email) {
        return jpaUserRepository.findByEmail(email);
    }

    @Override
    public UUID save(UserEntity user) {
        UserEntity userEntity = jpaUserRepository.save(user);

        return userEntity.getId();
    }
}

@Repository
interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);
}
