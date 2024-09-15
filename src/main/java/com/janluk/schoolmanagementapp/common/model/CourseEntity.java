package com.janluk.schoolmanagementapp.common.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseEntity extends BaseEntity implements Serializable {

    @ManyToOne
    private TeacherEntity teacher;

    @ManyToOne
    private SchoolSubjectEntity subject;

    @ManyToMany
    @JoinTable(name="courses_school_classes",
            joinColumns=
            @JoinColumn(name="course_id", referencedColumnName="id"),
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

    public void assignTeacher(TeacherEntity teacher) {
        this.teacher = teacher;
    }

    public void assignSubject(SchoolSubjectEntity subject) {
        this.subject = subject;
    }
}
