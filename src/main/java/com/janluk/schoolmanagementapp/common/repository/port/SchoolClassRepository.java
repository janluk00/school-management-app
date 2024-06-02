package com.janluk.schoolmanagementapp.common.repository.port;

import com.janluk.schoolmanagementapp.common.model.SchoolClassEntity;

public interface SchoolClassRepository {

    SchoolClassEntity getById(String name);
}
