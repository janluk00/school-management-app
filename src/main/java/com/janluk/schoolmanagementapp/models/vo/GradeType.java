package com.janluk.schoolmanagementapp.models.vo;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public enum GradeType {

    A(BigDecimal.valueOf(6)),
    A_MINUS(BigDecimal.valueOf(5.75)),

    B_PLUS(BigDecimal.valueOf(5.25)),
    B(BigDecimal.valueOf(5)),
    B_MINUS(BigDecimal.valueOf(4.75)),

    C_PLUS(BigDecimal.valueOf(4.25)),
    C(BigDecimal.valueOf(4)),
    C_MINUS(BigDecimal.valueOf(3.75)),

    D_PLUS(BigDecimal.valueOf(3.25)),
    D(BigDecimal.valueOf(3)),
    D_MINUS(BigDecimal.valueOf(2.75)),

    E_PLUS(BigDecimal.valueOf(2.25)),
    E(BigDecimal.valueOf(2)),
    E_MINUS(BigDecimal.valueOf(1.75)),

    F_PLUS(BigDecimal.valueOf(1.25)),
    F(BigDecimal.valueOf(1));

    public final BigDecimal value;
}
