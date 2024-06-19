package com.janluk.schoolmanagementapp.common.repository.port;

import com.janluk.schoolmanagementapp.common.model.StudentEntity;

import java.util.List;

public interface StudentRepository {

    List<StudentEntity> getAllInSchoolClass(String schoolClass);
}
