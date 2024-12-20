package com.janluk.schoolmanagementapp.common.repository.port;

import com.janluk.schoolmanagementapp.common.model.SchoolSubjectEntity;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.common.schema.TaughtSubjectDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SchoolSubjectRepository {

    SchoolSubjectEntity getById(SubjectType subject);

    Page<SchoolSubjectEntity> getAll(Pageable pageable);

    List<TaughtSubjectDTO> getAllTaughtSubjectsInSchoolClass(String schoolClass);
}
