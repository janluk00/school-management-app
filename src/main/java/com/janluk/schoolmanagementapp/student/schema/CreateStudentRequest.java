package com.janluk.schoolmanagementapp.student.schema;

import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.schema.CreateUserRequest;

// TODO: ADD VALIDATION
public record CreateStudentRequest(
        ClassType schoolClass,
        CreateUserRequest user
) {
}
