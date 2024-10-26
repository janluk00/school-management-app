package com.janluk.schoolmanagementapp.teacher.exception;

public class TeacherIsAlreadyTutorException extends RuntimeException {

    public TeacherIsAlreadyTutorException(String teacherId, String className) {
        super("Teacher with id: %s is already the tutor of class: %s.".formatted(teacherId, className));
    }
}
