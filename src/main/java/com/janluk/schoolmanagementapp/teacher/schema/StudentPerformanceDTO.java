package com.janluk.schoolmanagementapp.teacher.schema;

import com.janluk.schoolmanagementapp.common.schema.UserPersonalDTO;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record StudentPerformanceDTO(
        UUID id,
        UserPersonalDTO user,
        List<BigDecimal> grades
) {
}
