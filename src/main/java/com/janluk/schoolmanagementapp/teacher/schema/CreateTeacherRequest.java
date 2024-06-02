package com.janluk.schoolmanagementapp.teacher.schema;

import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.common.schema.CreateUserRequest;

import java.util.Set;

// TODO: ADD VALIDATION
public record CreateTeacherRequest(
        Set<SubjectType> taughtSubjects,
        CreateUserRequest user
) {
}
