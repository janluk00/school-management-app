package com.janluk.schoolmanagementapp.schoolClass.exception;

public class TeacherInCourseNotAssignedToSchoolClassException extends RuntimeException {

    public TeacherInCourseNotAssignedToSchoolClassException(String teacherId, String schoolClass) {
        super("Teacher in course with id: %s does not teach school class: %s".formatted(teacherId, schoolClass));
    }
}
