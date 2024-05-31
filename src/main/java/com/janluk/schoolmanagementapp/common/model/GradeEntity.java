package com.janluk.schoolmanagementapp.common.model;

import com.janluk.schoolmanagementapp.common.model.vo.GradeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "grades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GradeEntity implements Serializable {

    @Id
    private UUID id;

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
