package com.janluk.schoolmanagementapp.common.repository.port;

import com.janluk.schoolmanagementapp.common.model.TeacherEntity;

import java.util.UUID;

public interface TeacherRepository {

    TeacherEntity getById(UUID id);

    UUID save (TeacherEntity teacher);
}
