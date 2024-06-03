package com.janluk.schoolmanagementapp.teacher.exception;

public class TeacherIsAlreadyTutor extends RuntimeException {

    public TeacherIsAlreadyTutor(String teacherId, String className) {
        super("Teacher with id: %s is already the tutor of class %s.".formatted(teacherId, className));
    }
}
