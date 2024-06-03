package com.janluk.schoolmanagementapp.teacher.exception;

public class TeacherAlreadyTeachingSubjectException extends RuntimeException {

    public TeacherAlreadyTeachingSubjectException(String teacherId, String schoolSubject) {
        super("Teacher with id: %s is already teaching subject: %s.".formatted(teacherId, schoolSubject));
    }
}
