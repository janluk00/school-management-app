package com.janluk.schoolmanagementapp.common.model.vo;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.time.Period;

@Embeddable
public record BirthDate(LocalDate birthDate) {

    public static final int MIN_AGE_REQUIREMENT = 8;

    public BirthDate {
        if (birthDate.isBefore(LocalDate.of(1910, 1, 1)))
            throw new IllegalArgumentException("Date of birth cannot be earlier than 01.01.1910.");

        if (getUserAge(birthDate) < MIN_AGE_REQUIREMENT) {
            throw new IllegalArgumentException(
                    "The application is intended for users aged %d years and above.".formatted(MIN_AGE_REQUIREMENT)
            );
        }
    }

    private int getUserAge(LocalDate date) {
        Period age = Period.between(date, LocalDate.now());

        return age.getYears();
    }
}
