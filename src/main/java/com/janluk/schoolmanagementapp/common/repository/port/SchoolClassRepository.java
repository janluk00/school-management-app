package com.janluk.schoolmanagementapp.common.repository.port;

import com.janluk.schoolmanagementapp.common.model.SchoolClassEntity;
import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SchoolClassRepository {

    SchoolClassEntity getById(ClassType schoolClass);

    Page<SchoolClassEntity> getAll(Pageable pageable);
}
