package com.janluk.schoolmanagementapp.models.vo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SubjectType {

    MATHEMATICS("TU WSTAW Z PLIKU typu {subject.math}"),
    BIOLOGY("TU WSTAW Z PLIKU typu {subject.math}"),
    CHEMISTRY("TU WSTAW Z PLIKU typu {subject.math}"),
    PHYSICS("TU WSTAW Z PLIKU typu {subject.math}"),
    GEOGRAPHY("TU WSTAW Z PLIKU typu {subject.math}"),
    ENGLISH("TU WSTAW Z PLIKU typu {subject.math}");

    public final String type;
}
