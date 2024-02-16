package com.janluk.schoolmanagementapp.models;

import com.janluk.schoolmanagementapp.common.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "teacher_taught_subjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherTaughtSubject extends BaseModel {

    @ManyToOne
    private Teacher teacher;

    @ManyToOne
    private Subject subject;

    @ManyToMany
    @JoinTable(name="teacher_taught_subjects_school_classes",
            joinColumns=
            @JoinColumn(name="teacher_taught_subject_id", referencedColumnName="id"),
            inverseJoinColumns=
            @JoinColumn(name="school_class_id", referencedColumnName="id")
    )
    private Set<SchoolClass> schoolClasses;
}
