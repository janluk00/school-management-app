package com.janluk.schoolmanagementapp.subject.mapper;

import com.janluk.schoolmanagementapp.common.model.SchoolSubjectEntity;
import com.janluk.schoolmanagementapp.subject.schema.SchoolSubjectDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class SchoolSubjectMapper {

    public Page<SchoolSubjectDTO> pageSchoolSubjectEntitiesToSchoolSubjectDTOs(Page<SchoolSubjectEntity> subjects) {
        return subjects.map(this::schoolSubjectEntityToSchoolSubjectDTO);
    }

    private SchoolSubjectDTO schoolSubjectEntityToSchoolSubjectDTO(SchoolSubjectEntity subject) {
        return new SchoolSubjectDTO(subject.getName());
    }
}
