package com.janluk.schoolmanagementapp.common.model;

import com.janluk.schoolmanagementapp.common.model.protocol.RoleAssignable;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherEntity implements Serializable, RoleAssignable {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "school_user_id", referencedColumnName = "id")
    private UserEntity user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tutor_class_name", referencedColumnName = "name")
    private SchoolClassEntity tutorClass;

    @OneToMany(mappedBy = "teacherId")
    private Set<GradeEntity> gradesIssued;

    @OneToMany(mappedBy = "teacher")
    private Set<TeacherInCourseEntity> teacherTaughtSubjects;

    @ManyToMany
    @JoinTable(name="teachers_subjects",
            joinColumns=
            @JoinColumn(name="teacher_id", referencedColumnName="id"),
            inverseJoinColumns=
            @JoinColumn(name="subject_name", referencedColumnName="name")
    )
    private Set<SchoolSubjectEntity> taughtSubjects;

    @Override
    public Set<RoleEntity> getRoles() {
        return this.user.getRoles();
    }

    public void assignTutor(SchoolClassEntity schoolClass) {
        this.tutorClass = schoolClass;
    }
}