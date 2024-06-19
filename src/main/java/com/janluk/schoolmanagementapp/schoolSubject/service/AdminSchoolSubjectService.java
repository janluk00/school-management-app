package com.janluk.schoolmanagementapp.schoolSubject.service;

import com.janluk.schoolmanagementapp.common.repository.port.SchoolSubjectRepository;
import com.janluk.schoolmanagementapp.schoolSubject.mapper.SchoolSubjectMapper;
import com.janluk.schoolmanagementapp.schoolSubject.schema.SchoolSubjectDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminSchoolSubjectService {

    private final SchoolSubjectRepository schoolSubjectRepository;
    private final SchoolSubjectMapper schoolSubjectMapper;

    public Page<SchoolSubjectDTO> getAllSchoolSubjects(Pageable pageable) {
        return schoolSubjectMapper.pageSchoolSubjectEntitiesToSchoolSubjectDTOs(
                schoolSubjectRepository.getAll(pageable)
        );
    }
}
