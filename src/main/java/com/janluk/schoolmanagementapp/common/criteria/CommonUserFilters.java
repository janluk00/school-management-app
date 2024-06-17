package com.janluk.schoolmanagementapp.common.criteria;

public record CommonUserFilters(
        String name,
        String surname,
        String birth_date_lte,
        String birth_date_gte
) {
}
