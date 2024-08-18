package com.janluk.schoolmanagementapp.common.model;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@MappedSuperclass
@EqualsAndHashCode(of = "id")
public abstract class BaseEntity {

    @Id
    @Getter
    private UUID id = UUID.randomUUID();
}
