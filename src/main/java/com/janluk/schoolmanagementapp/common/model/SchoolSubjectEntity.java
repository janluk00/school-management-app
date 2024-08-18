package com.janluk.schoolmanagementapp.common.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "school_subjects")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "name")
public class SchoolSubjectEntity implements Serializable {

    @Id
    private String name;

    @OneToMany(mappedBy = "subjectName")
    private Set<GradeEntity> grades;

    @ManyToMany(mappedBy = "taughtSubjects")
    @Builder.Default
    private Set<TeacherEntity> teachers = new HashSet<>();

    @OneToMany(mappedBy = "subject")
    private Set<TeacherInCourseEntity> subjectTeachers;
}