package com.janluk.schoolmanagementapp.common.user;

import com.janluk.schoolmanagementapp.common.model.RoleEntity;
import com.janluk.schoolmanagementapp.common.model.StudentEntity;
import com.janluk.schoolmanagementapp.common.model.TeacherEntity;
import com.janluk.schoolmanagementapp.common.model.UserEntity;
import com.janluk.schoolmanagementapp.common.model.protocol.RoleAssignable;
import com.janluk.schoolmanagementapp.common.model.vo.RoleType;
import com.janluk.schoolmanagementapp.common.repository.port.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleAdder {

    private final RoleRepository roleRepository;

    public void addAdminRole(UserEntity user) {
        addRole(user, RoleType.ROLE_ADMIN);
    }

    public void addTeacherRole(TeacherEntity teacher) {
        addRole(teacher, RoleType.ROLE_TEACHER);
    }

    public void addStudentRole(StudentEntity student) {
        addRole(student, RoleType.ROLE_STUDENT);
    }

    private void addRole(RoleAssignable entity, RoleType roleType) {
        RoleEntity role = roleRepository.getById(roleType.name());

        entity.getUser().getRoles().add(role);
    }
}
