package com.janluk.schoolmanagementapp.security.schema;

import com.janluk.schoolmanagementapp.common.annotation.PasswordsMatch;

@PasswordsMatch
public record ConfirmPasswordRequest(String password, String confirmPassword) {
}
