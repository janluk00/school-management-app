package com.janluk.schoolmanagementapp.teacher.schema;

import com.janluk.schoolmanagementapp.common.schema.UserBaseInformationDTO;
import lombok.Builder;

import java.util.UUID;

@Builder
public record TeacherSearchDTO(
        UUID id,
        UserBaseInformationDTO user
) {
}
