package com.janluk.schoolmanagementapp.exception;

import com.janluk.schoolmanagementapp.common.exception.DatePatternException;
import com.janluk.schoolmanagementapp.common.exception.EmailAlreadyExistsException;
import com.janluk.schoolmanagementapp.common.exception.NoResultFoundException;
import com.janluk.schoolmanagementapp.common.exception.TeacherNotTeachingSubjectException;
import com.janluk.schoolmanagementapp.schoolClass.exception.SchoolClassAlreadyHasTeacherOfSchoolSubjectException;
import com.janluk.schoolmanagementapp.schoolClass.exception.CourseNotAssignedToSchoolClassException;
import com.janluk.schoolmanagementapp.teacher.exception.TeacherAlreadyTeachingSubjectException;
import com.janluk.schoolmanagementapp.teacher.exception.TeacherIsAlreadyTutorException;
import com.janluk.schoolmanagementapp.teacher.exception.TeacherNotAssignedAsTutorException;
import com.janluk.schoolmanagementapp.teacher.exception.TeacherNotInCourseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class GlobalExceptionHandler {

    private static final String AUTHENTICATION_MESSAGE = "Wrong email or password!";

    @ExceptionHandler(
            value = {
                    DatePatternException.class,
                    TeacherAlreadyTeachingSubjectException.class,
                    HttpMessageNotReadableException.class
            }
    )
    public ResponseEntity<ErrorResponse> handleBadRequestException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .httpStatus(HttpStatus.BAD_REQUEST)
                                .message(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException manve) {
        String methodArgumentErrors = ValidationErrorProcessor.processFieldErrors(manve.getBindingResult());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .statusCode(HttpStatus.FORBIDDEN.value())
                                .httpStatus(HttpStatus.FORBIDDEN)
                                .message(methodArgumentErrors)
                                .build()
                );
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        ErrorResponse.builder()
                                .statusCode(HttpStatus.UNAUTHORIZED.value())
                                .httpStatus(HttpStatus.UNAUTHORIZED)
                                .message(AUTHENTICATION_MESSAGE)
                                .build()
                );
    }

    @ExceptionHandler(
            value = {
                    TeacherNotTeachingSubjectException.class,
                    TeacherNotInCourseException.class,
                    TeacherNotAssignedAsTutorException.class
            }
    )
    public ResponseEntity<ErrorResponse> handleForbiddenException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(
                        ErrorResponse.builder()
                                .statusCode(HttpStatus.FORBIDDEN.value())
                                .httpStatus(HttpStatus.FORBIDDEN)
                                .message(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = NoResultFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        ErrorResponse.builder()
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .httpStatus(HttpStatus.NOT_FOUND)
                                .message(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(
            value = {
                    EmailAlreadyExistsException.class,
                    SchoolClassAlreadyHasTeacherOfSchoolSubjectException.class,
                    CourseNotAssignedToSchoolClassException.class,
                    TeacherIsAlreadyTutorException.class
            }
    )
    public ResponseEntity<ErrorResponse> handleConflictException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                        ErrorResponse.builder()
                                .statusCode(HttpStatus.CONFLICT.value())
                                .httpStatus(HttpStatus.CONFLICT)
                                .message(exception.getMessage())
                                .build()
                );
    }
}
