package com.janluk.schoolmanagementapp.subject.exception;

public class SchoolClassAlreadyHasTeacherOfSchoolSubjectException extends RuntimeException {

    public SchoolClassAlreadyHasTeacherOfSchoolSubjectException(String schoolClass, String schoolSubject) {
        super("School class with name: %s already has a teacher of school subject with name: %s"
                .formatted(schoolClass, schoolSubject));
    }
}
