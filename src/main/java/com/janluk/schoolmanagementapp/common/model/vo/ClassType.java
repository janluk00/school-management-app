package com.janluk.schoolmanagementapp.common.model.vo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ClassType {

    A1("1A"),
    A2("2A"),
    A3("3A"),

    B1("1B"),
    B2("2B"),
    B3("3B"),

    C1("1C"),
    C2("2C"),
    C3("3C");

    public final String type;
}
