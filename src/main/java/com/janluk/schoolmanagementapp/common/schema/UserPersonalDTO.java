package com.janluk.schoolmanagementapp.common.schema;

import lombok.Builder;

@Builder
public record UserPersonalDTO(
        String name,
        String surname
) {
}
