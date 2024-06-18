package com.janluk.schoolmanagementapp.common.repository.port;

import com.janluk.schoolmanagementapp.common.model.SchoolSubjectEntity;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SchoolSubjectRepository {

    SchoolSubjectEntity getById(SubjectType subject);

    Page<SchoolSubjectEntity> getAll(Pageable pageable);
}
