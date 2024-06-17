package com.janluk.schoolmanagementapp.common.repository.port;

import com.janluk.schoolmanagementapp.common.model.SchoolClassEntity;
import com.janluk.schoolmanagementapp.common.model.vo.ClassType;

public interface SchoolClassRepository {

    SchoolClassEntity getById(ClassType schoolClass);
}
