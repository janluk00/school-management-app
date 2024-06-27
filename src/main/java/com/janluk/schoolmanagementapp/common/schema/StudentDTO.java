package com.janluk.schoolmanagementapp.common.schema;

import lombok.Builder;

import java.util.UUID;

@Builder
public record StudentDTO(
        UUID id,
        String schoolClass,
        UserBaseInformationDTO user
) {
}
