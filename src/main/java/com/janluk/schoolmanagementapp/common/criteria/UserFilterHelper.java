package com.janluk.schoolmanagementapp.common.criteria;

import com.janluk.schoolmanagementapp.common.model.UserEntity;
import com.janluk.schoolmanagementapp.common.model.protocol.RoleAssignable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

import java.util.List;

import static com.janluk.schoolmanagementapp.common.criteria.UserFields.*;

public class UserFilterHelper {

    public static void addCommonUserFilters(
            Join<? extends RoleAssignable, UserEntity> userJoin,
            CriteriaBuilder cb,
            List<Predicate> predicates,
            CommonUserFilters userCriteria
            ) {
        if (userCriteria.name() != null) {
            OperationHelper.doInclude(NAME, userCriteria.name(), predicates, userJoin, cb);
        }

        if (userCriteria.surname() != null) {
            OperationHelper.doInclude(SURNAME, userCriteria.surname(), predicates, userJoin, cb);
        }

        if (userCriteria.birth_date_lte() != null) {
            OperationHelper.doLte(BIRTH_DATE, userCriteria.birth_date_lte(), predicates, userJoin, cb);
        }

        if (userCriteria.birth_date_gte() != null) {
            OperationHelper.doGte(BIRTH_DATE, userCriteria.birth_date_gte(), predicates, userJoin, cb);
        }
    }
}
