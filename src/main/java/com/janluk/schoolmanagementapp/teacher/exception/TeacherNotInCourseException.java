package com.janluk.schoolmanagementapp.teacher.exception;

public class TeacherNotInCourseException extends RuntimeException {

    public TeacherNotInCourseException(String email, String schoolSubject, String schoolClass) {
        super("Teacher with email %s is not assigned to course with school subject %s and school class %s."
                .formatted(email, schoolSubject, schoolClass));
    }
}
