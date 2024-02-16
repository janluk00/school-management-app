package com.janluk.schoolmanagementapp.models;

import com.janluk.schoolmanagementapp.common.BaseModel;
import com.janluk.schoolmanagementapp.models.vo.ClassType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "school_classes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchoolClass extends BaseModel {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClassType name;

    @ManyToMany(mappedBy = "schoolClasses")
    private Set<TeacherTaughtSubject> teachers;
}
