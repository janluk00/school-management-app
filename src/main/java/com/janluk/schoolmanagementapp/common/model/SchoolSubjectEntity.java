package com.janluk.schoolmanagementapp.common.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "school_subjects")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SchoolSubjectEntity implements Serializable {

    @Id
    private String name;

    @OneToMany(mappedBy = "subjectName")
    private Set<GradeEntity> grades;

    @ManyToMany(mappedBy = "taughtSubjects", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<TeacherEntity> teachers = new HashSet<>();

    @OneToMany(mappedBy = "subject")
    private Set<TeacherTaughtSubjectEntity> subjectTeachers;
}