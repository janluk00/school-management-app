package com.janluk.schoolmanagementapp.common.model.protocol;

import com.janluk.schoolmanagementapp.common.model.RoleEntity;
import com.janluk.schoolmanagementapp.common.model.UserEntity;

import java.util.Set;

public interface RoleAssignable {

    UserEntity getUser();

    Set<RoleEntity> getRoles();
}
