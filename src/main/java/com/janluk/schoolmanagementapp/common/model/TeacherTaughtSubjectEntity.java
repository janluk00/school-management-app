package com.janluk.schoolmanagementapp.common.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "teacher_taught_subjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherTaughtSubjectEntity implements Serializable {

    @Id
    private UUID id;

    @ManyToOne
    private TeacherEntity teacher;

    @ManyToOne
    private SchoolSubjectEntity subject;

    @ManyToMany
    @JoinTable(name="teacher_taught_subjects_school_classes",
            joinColumns=
            @JoinColumn(name="teacher_taught_subject_id", referencedColumnName="id"),
            inverseJoinColumns=
            @JoinColumn(name="school_class_name", referencedColumnName="name")
    )
    private Set<SchoolClassEntity> schoolClasses;
}
