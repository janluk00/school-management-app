package com.janluk.schoolmanagementapp.schoolClass.mapper;

import com.janluk.schoolmanagementapp.common.model.SchoolClassEntity;
import com.janluk.schoolmanagementapp.schoolClass.schema.SchoolClassDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class SchoolClassMapper {

    public Page<SchoolClassDTO> pageSchoolClassEntitiesToSchoolClassDTOs(Page<SchoolClassEntity> classes) {
        return classes.map(this::schoolClassEntityToSchoolClassDTO);
    }

    private SchoolClassDTO schoolClassEntityToSchoolClassDTO(SchoolClassEntity schoolClass) {
        return new SchoolClassDTO(schoolClass.getName());
    }
}
