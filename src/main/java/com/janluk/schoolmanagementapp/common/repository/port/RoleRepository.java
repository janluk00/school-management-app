package com.janluk.schoolmanagementapp.common.repository.port;

import com.janluk.schoolmanagementapp.common.model.RoleEntity;

public interface RoleRepository {

    RoleEntity getByRole(String role);
}
