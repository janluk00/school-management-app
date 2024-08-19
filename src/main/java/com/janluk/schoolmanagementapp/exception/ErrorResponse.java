package com.janluk.schoolmanagementapp.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
record ErrorResponse(
        Integer statusCode,
        HttpStatus httpStatus,
        String message
) {
}
