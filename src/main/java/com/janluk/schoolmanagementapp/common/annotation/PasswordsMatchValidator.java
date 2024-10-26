package com.janluk.schoolmanagementapp.common.annotation;

import com.janluk.schoolmanagementapp.security.schema.ConfirmPasswordRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, ConfirmPasswordRequest> {

    @Override
    public void initialize(PasswordsMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ConfirmPasswordRequest confirmPasswordRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (confirmPasswordRequest == null)
            return false;

        return confirmPasswordRequest.password().equals(confirmPasswordRequest.confirmPassword());
    }
}
