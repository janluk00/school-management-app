package com.janluk.schoolmanagementapp.common.repository.adapter;

import com.janluk.schoolmanagementapp.common.exception.NoResultFoundException;
import com.janluk.schoolmanagementapp.common.model.RoleEntity;
import com.janluk.schoolmanagementapp.common.repository.port.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class SqlRoleRepository implements RoleRepository{

    private final JpaRoleRepository jpaRoleRepository;

    @Override
    public RoleEntity getById(String role) {
        return jpaRoleRepository.findById(role)
                .orElseThrow(() -> new NoResultFoundException("Could not find role: %s".formatted(role)));
    }
}

@Repository
interface JpaRoleRepository extends JpaRepository<RoleEntity, String> {

}
