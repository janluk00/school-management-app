package com.janluk.schoolmanagementapp.common.repository.adapter;

import com.janluk.schoolmanagementapp.common.exception.NoResultFoundException;
import com.janluk.schoolmanagementapp.common.model.SchoolSubjectEntity;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.common.repository.port.SchoolSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SqlSchoolSubjectRepository implements SchoolSubjectRepository {

    private final JpaSchoolSubjectRepository jpaSchoolSubjectRepository;

    @Override
    public SchoolSubjectEntity getById(SubjectType subject) {
        return jpaSchoolSubjectRepository.findById(subject.name())
                .orElseThrow(() -> new NoResultFoundException(
                        "Could not find school subject with name: %s".formatted(subject.name())
                    )
                );
    }

    @Override
    public Page<SchoolSubjectEntity> getAll(Pageable pageable) {
        return jpaSchoolSubjectRepository.findAll(pageable);
    }
}

@Repository
interface JpaSchoolSubjectRepository extends JpaRepository<SchoolSubjectEntity, String> {
}