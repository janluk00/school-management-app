package com.janluk.schoolmanagementapp.models;

import com.janluk.schoolmanagementapp.common.BaseModel;
import com.janluk.schoolmanagementapp.models.vo.SubjectType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subject extends BaseModel {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SubjectType name;

    @OneToMany(mappedBy = "subjectId")
    private Set<Grade> grades;

    @OneToMany(mappedBy = "subject")
    private Set<TeacherTaughtSubject> subjectTeachers;
}
