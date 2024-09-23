package com.janluk.schoolmanagementapp.schoolClass.service;

import com.janluk.schoolmanagementapp.common.repository.port.*;
import com.janluk.schoolmanagementapp.schoolClass.mapper.SchoolClassMapper;
import com.janluk.schoolmanagementapp.schoolClass.schema.SchoolClassDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminSchoolClassService {

    private final SchoolClassRepository schoolClassRepository;
    private final SchoolClassMapper schoolClassMapper;

    public Page<SchoolClassDTO> getAllSchoolClasses(Pageable pageable) {
        return schoolClassMapper.pageSchoolClassEntitiesToSchoolClassDTOs(schoolClassRepository.getAll(pageable));
    }
}
