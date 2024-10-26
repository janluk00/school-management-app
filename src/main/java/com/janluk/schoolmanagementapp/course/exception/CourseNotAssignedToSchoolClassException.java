package com.janluk.schoolmanagementapp.course.exception;

public class CourseNotAssignedToSchoolClassException extends RuntimeException {

    public CourseNotAssignedToSchoolClassException(String courseId, String schoolClass) {
        super("School class: %s does not participate in the course with id: %s".formatted(schoolClass, courseId));
    }
}
