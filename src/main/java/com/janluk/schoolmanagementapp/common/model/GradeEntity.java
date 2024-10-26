package com.janluk.schoolmanagementapp.common.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "grades")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradeEntity extends BaseEntity implements Serializable {

    @Column(nullable = false)
    private BigDecimal grade;

    @Column(nullable = false)
    private Instant issuedDate;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private UUID teacherId;

    @Column(nullable = false)
    private String subjectName;
}
