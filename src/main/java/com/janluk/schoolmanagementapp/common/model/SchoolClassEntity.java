package com.janluk.schoolmanagementapp.common.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "school_classes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolClassEntity implements Serializable {

    @Id
    private String name;

    @OneToMany(mappedBy = "schoolClass")
    private Set<StudentEntity> students;

    @ManyToMany(mappedBy = "schoolClasses")
    private Set<TeacherInCourseEntity> teachers;
}