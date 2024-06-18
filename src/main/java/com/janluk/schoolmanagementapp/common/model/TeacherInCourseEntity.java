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
@Table(name = "teachers_in_course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherInCourseEntity implements Serializable {

    @Id
    private UUID id;

    @ManyToOne
    private TeacherEntity teacher;

    @ManyToOne
    private SchoolSubjectEntity subject;

    @ManyToMany
    @JoinTable(name="teachers_in_course_school_classes",
            joinColumns=
            @JoinColumn(name="teacher_taught_subject_id", referencedColumnName="id"),
            inverseJoinColumns=
            @JoinColumn(name="school_class_name", referencedColumnName="name")
    )
    private Set<SchoolClassEntity> schoolClasses;
}
