package com.janluk.schoolmanagementapp.common.schema;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record UserBaseInformationDTO(
        UUID id,
        String name,
        String surname,
        LocalDate birthDate
) {
}
