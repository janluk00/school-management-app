package com.janluk.schoolmanagementapp.common.repository.adapter;

import com.janluk.schoolmanagementapp.common.exception.NoResultFoundException;
import com.janluk.schoolmanagementapp.common.model.RoleEntity;
import com.janluk.schoolmanagementapp.common.repository.port.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SqlRoleRepository implements RoleRepository{

    private final JpaRoleRepository jpaRoleRepository;

    @Override
    public RoleEntity getByRole(String role) {
        return jpaRoleRepository.findByRole(role)
                .orElseThrow(() -> new NoResultFoundException("Could not find role: %s".formatted(role)));
    }
}

interface JpaRoleRepository extends JpaRepository<RoleEntity, UUID> {

    Optional<RoleEntity> findByRole(String role);
}
