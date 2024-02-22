package com.janluk.schoolmanagementapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherEntity implements Serializable {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "school_user_id", referencedColumnName = "id")
    private UserEntity user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "school_class_id", referencedColumnName = "id")
    private SchoolClassEntity tutorClass;

    @OneToMany(mappedBy = "teacherId")
    private Set<GradeEntity> gradesIssued;

    @OneToMany(mappedBy = "teacher")
    private Set<TeacherTaughtSubjectEntity> taughtSubjects;
}
