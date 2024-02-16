package com.janluk.schoolmanagementapp.models;

import com.janluk.schoolmanagementapp.common.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Teacher extends BaseModel {

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "school_user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "school_class_id", referencedColumnName = "id")
    private SchoolClass tutorClass;

    @OneToMany(mappedBy = "teacherId")
    private Set<Grade> gradesIssued;

    @OneToMany(mappedBy = "teacher")
    private Set<TeacherTaughtSubject> taughtSubjects;
}
