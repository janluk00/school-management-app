package com.janluk.schoolmanagementapp.teacher.criteria;

import com.janluk.schoolmanagementapp.common.criteria.CommonUserFilters;
import com.janluk.schoolmanagementapp.common.criteria.UserFilterHelper;
import com.janluk.schoolmanagementapp.common.model.TeacherEntity;
import com.janluk.schoolmanagementapp.common.model.UserEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/*
This class has been implemented in case in the future filters related to teacher would be necessary
*/
public class TeacherSpecifications {

    public static Specification<TeacherEntity> withTeacherFilters(
            CommonUserFilters userFilters
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<TeacherEntity, UserEntity> userJoin = root.join(TeacherFields.USER, JoinType.INNER);

            UserFilterHelper.addCommonUserFilters(userJoin, criteriaBuilder, predicates, userFilters);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
