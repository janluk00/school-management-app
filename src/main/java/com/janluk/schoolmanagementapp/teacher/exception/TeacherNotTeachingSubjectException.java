package com.janluk.schoolmanagementapp.teacher.exception;

public class TeacherNotTeachingSubjectException extends RuntimeException {

    public TeacherNotTeachingSubjectException(String teacherId, String schoolSubject) {
        super("Teacher with id: %s is not teaching subject: %s.".formatted(teacherId, schoolSubject));
    }
}
