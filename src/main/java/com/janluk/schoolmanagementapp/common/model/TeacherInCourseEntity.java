package com.janluk.schoolmanagementapp.common.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teachers_in_course")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherInCourseEntity extends BaseEntity implements Serializable {

    @Setter
    @ManyToOne
    private TeacherEntity teacher;

    @Setter
    @ManyToOne
    private SchoolSubjectEntity subject;

    @ManyToMany
    @JoinTable(name="teachers_in_course_school_classes",
            joinColumns=
            @JoinColumn(name="teacher_in_course_id", referencedColumnName="id"),
            inverseJoinColumns=
            @JoinColumn(name="school_class_name", referencedColumnName="name")
    )
    @Builder.Default
    private Set<SchoolClassEntity> schoolClasses = new HashSet<>();

    public void assignToClass(SchoolClassEntity schoolClass) {
        if (this.schoolClasses.contains(schoolClass)) {
            throw new IllegalArgumentException(
                    "Teacher already teaches school class with name: %s"
                            .formatted(schoolClass.getName())
            );
        }

        this.schoolClasses.add(schoolClass);
    }

    public void removeFromClass(SchoolClassEntity schoolClass) {
        if (!this.schoolClasses.contains(schoolClass)) {
            throw new IllegalArgumentException(
                    "Teacher does not teach school class with name: %s"
                            .formatted(schoolClass.getName())
            );
        }

        this.schoolClasses.remove(schoolClass);
    }
}
