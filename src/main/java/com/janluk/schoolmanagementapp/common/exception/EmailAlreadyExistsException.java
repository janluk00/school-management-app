package com.janluk.schoolmanagementapp.common.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("Email: %s already in use.".formatted(email));
    }
}
