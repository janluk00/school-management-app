package com.janluk.schoolmanagementapp.student.criteria;

import com.janluk.schoolmanagementapp.common.criteria.CommonUserFilters;
import com.janluk.schoolmanagementapp.common.criteria.UserFilterHelper;
import com.janluk.schoolmanagementapp.common.model.StudentEntity;
import com.janluk.schoolmanagementapp.common.model.UserEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/*
This class has been implemented in case in the future filters related to student would be necessary
*/
public class StudentSpecifications {

    public static Specification<StudentEntity> withStudentFilters(
            CommonUserFilters userFilters
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<StudentEntity, UserEntity> userJoin = root.join(StudentFields.USER, JoinType.INNER);

            UserFilterHelper.addCommonUserFilters(userJoin, criteriaBuilder, predicates, userFilters);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
