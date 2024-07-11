package com.janluk.schoolmanagementapp.teacher.schema;

import com.janluk.schoolmanagementapp.common.model.vo.GradeType;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;

// TODO: ADD VALIDATION
public record AddGradeRequest(
        GradeType grade,
        SubjectType subjectType
) {
}
