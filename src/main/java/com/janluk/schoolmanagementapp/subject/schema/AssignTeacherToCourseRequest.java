package com.janluk.schoolmanagementapp.subject.schema;

import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;

import java.util.UUID;

public record AssignTeacherToCourseRequest(
        ClassType classType,
        SubjectType subjectType,
        UUID teacherId
) {
}
