package com.janluk.schoolmanagementapp.teacher.exception;

public class TeacherNotAssignedAsTutorException extends RuntimeException {

    public TeacherNotAssignedAsTutorException(String teacherId) {
        super("Teacher with id %s is not assigned as a tutor to any school class.".formatted(teacherId));
    }
}