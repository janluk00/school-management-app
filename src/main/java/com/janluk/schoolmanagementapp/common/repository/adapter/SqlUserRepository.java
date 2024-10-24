package com.janluk.schoolmanagementapp.common.repository.adapter;

import com.janluk.schoolmanagementapp.common.exception.NoResultFoundException;
import com.janluk.schoolmanagementapp.common.model.UserEntity;

import com.janluk.schoolmanagementapp.common.repository.port.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
class SqlUserRepository implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    public SqlUserRepository(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public UserEntity getByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
                .orElseThrow(() -> new NoResultFoundException("Could not find user with e-mail: %s".formatted(email)));
    }

    @Override
    public UserEntity getByEmailWithRoles(String email) {
        return jpaUserRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new NoResultFoundException("Could not find user with e-mail: %s".formatted(email)));
    }

    @Override
    public UserEntity getByPasswordConfirmationToken(String passwordConfirmationToken) {
        return jpaUserRepository.findByPasswordConfirmationToken(passwordConfirmationToken)
                .orElseThrow(() -> new NoResultFoundException(
                        "Could not find user with token: %s".formatted(passwordConfirmationToken)
                    )
                );
    }

    @Override
    public UUID save(UserEntity user) {
        UserEntity userEntity = jpaUserRepository.save(user);

        return userEntity.getId();
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }
}

@Repository
interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT u FROM UserEntity u JOIN FETCH u.roles WHERE u.email = ?1")
    Optional<UserEntity> findByEmailWithRoles(String email);

    Optional<UserEntity> findByPasswordConfirmationToken(String passwordConfirmationToken);

    boolean existsByEmail(String email);
}
