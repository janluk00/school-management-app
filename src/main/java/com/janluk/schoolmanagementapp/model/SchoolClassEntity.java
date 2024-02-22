package com.janluk.schoolmanagementapp.model;

import com.janluk.schoolmanagementapp.model.vo.ClassType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "school_classes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchoolClassEntity implements Serializable {

    @Id
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClassType name;

    @ManyToMany(mappedBy = "schoolClasses")
    private Set<TeacherTaughtSubjectEntity> teachers;
}
