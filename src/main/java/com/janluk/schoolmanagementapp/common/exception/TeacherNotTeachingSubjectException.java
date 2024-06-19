package com.janluk.schoolmanagementapp.common.exception;

public class TeacherNotTeachingSubjectException extends RuntimeException {

    public TeacherNotTeachingSubjectException(String teacherId, String schoolSubject) {
        super("Teacher with id: %s does not teach subject: %s.".formatted(teacherId, schoolSubject));
    }
}
