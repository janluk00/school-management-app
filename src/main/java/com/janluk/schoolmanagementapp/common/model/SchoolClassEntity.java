package com.janluk.schoolmanagementapp.common.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "school_classes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchoolClassEntity implements Serializable {

    @Id
    private String name;

    @OneToMany(mappedBy = "schoolClass")
    private Set<StudentEntity> students;

    @ManyToMany(mappedBy = "schoolClasses")
    private Set<TeacherTaughtSubjectEntity> teachers;
}