package com.janluk.schoolmanagementapp.common.repository.port;

import com.janluk.schoolmanagementapp.common.model.SchoolSubjectEntity;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;

public interface SchoolSubjectRepository {

    SchoolSubjectEntity getById(SubjectType subject);
}
