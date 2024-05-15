package com.janluk.schoolmanagementapp.common.exception;

public class NoSuchUserException extends RuntimeException{

    public NoSuchUserException(String message) {
        super(message);
    }
}
