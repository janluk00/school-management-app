package com.janluk.schoolmanagementapp.model;

import com.janluk.schoolmanagementapp.model.vo.SubjectType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectEntity implements Serializable {

    @Id
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SubjectType name;

    @OneToMany(mappedBy = "subjectId")
    private Set<GradeEntity> grades;

    @OneToMany(mappedBy = "subject")
    private Set<TeacherTaughtSubjectEntity> subjectTeachers;
}
