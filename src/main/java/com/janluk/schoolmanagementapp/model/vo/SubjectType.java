package com.janluk.schoolmanagementapp.model.vo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SubjectType {
    // TODO: Change hardcoded type to subject names from the resource file.

    MATHEMATICS("MATH"),
    BIOLOGY("BIOL"),
    CHEMISTRY("CHEM"),
    PHYSICS("PHYS"),
    GEOGRAPHY("GEOG"),
    ENGLISH("ENGL");

    public final String type;
}
