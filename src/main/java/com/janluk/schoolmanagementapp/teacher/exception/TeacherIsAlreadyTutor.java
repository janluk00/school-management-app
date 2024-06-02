package com.janluk.schoolmanagementapp.teacher.exception;

public class TeacherIsAlreadyTutor extends RuntimeException{

    public TeacherIsAlreadyTutor(String teacher, String className) {
        super("Teacher with id: %s is already the tutor of class %s.".formatted(teacher, className));
    }
}
