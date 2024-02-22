package com.janluk.schoolmanagementapp.repository.adapter;

import com.janluk.schoolmanagementapp.model.UserEntity;

import com.janluk.schoolmanagementapp.repository.port.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class SqlUserRepository implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    public SqlUserRepository(final JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }
}

@Repository
interface JpaUserRepository extends JpaRepository<UserEntity, UUID>{

}
