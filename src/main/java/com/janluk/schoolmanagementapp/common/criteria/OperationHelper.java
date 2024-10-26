package com.janluk.schoolmanagementapp.common.criteria;

import com.janluk.schoolmanagementapp.common.exception.DatePatternException;
import com.janluk.schoolmanagementapp.common.model.UserEntity;
import com.janluk.schoolmanagementapp.common.model.protocol.RoleAssignable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class OperationHelper {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    public static void doInclude(
            String column,
            String value,
            List<Predicate> predicates,
            Join<? extends RoleAssignable, UserEntity> userJoin,
            CriteriaBuilder cb
    ) {
        Predicate predicate = cb.like(cb.lower(userJoin.get(column)), "%" + value.toLowerCase() + "%");

        predicates.add(predicate);
    }

    public static void doEqual(
            String column,
            String value,
            List<Predicate> predicates,
            Join<? extends RoleAssignable, UserEntity> userJoin,
            CriteriaBuilder cb
    ) {
        Predicate predicate = cb.equal(userJoin.get(column), value);

        predicates.add(predicate);
    }

    public static void doLte(
            String column,
            String value,
            List<Predicate> predicates,
            Join<? extends RoleAssignable, UserEntity> userJoin,
            CriteriaBuilder cb
    ) {
        Predicate predicate = cb.lessThanOrEqualTo(userJoin.get(column), parseToLocalDate(value));

        predicates.add(predicate);
    }

    public static void doGte(
            String column,
            String value,
            List<Predicate> predicates,
            Join<? extends RoleAssignable, UserEntity> userJoin,
            CriteriaBuilder cb
    ) {
        Predicate predicate = cb.greaterThanOrEqualTo(userJoin.get(column), parseToLocalDate(value));

        predicates.add(predicate);
    }

    public static LocalDate parseToLocalDate(String date) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(DATE_PATTERN);

        try {
            return LocalDate.parse(date, pattern);
        } catch (DateTimeParseException e) {
            throw new DatePatternException("Wrong date pattern! Use: %s".formatted(DATE_PATTERN));
        }
    }
}
