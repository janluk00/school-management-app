package com.janluk.schoolmanagementapp.common.repository.adapter;


import com.janluk.schoolmanagementapp.common.exception.NoResultFoundException;
import com.janluk.schoolmanagementapp.common.model.SchoolClassEntity;
import com.janluk.schoolmanagementapp.common.repository.port.SchoolClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SqlSchoolClassRepository implements SchoolClassRepository {

    private final JpaSchoolClassRepository jpaSchoolClassRepository;

    @Override
    public SchoolClassEntity getById(String name) {
        return jpaSchoolClassRepository.findById(name)
                .orElseThrow(() -> new NoResultFoundException(
                        "Could not find school class with name: %s".formatted(name)
                    )
                );
    }
}

interface JpaSchoolClassRepository extends JpaRepository<SchoolClassEntity, String> {

}