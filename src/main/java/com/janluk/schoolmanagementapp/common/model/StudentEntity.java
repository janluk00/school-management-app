package com.janluk.schoolmanagementapp.common.model;

import com.janluk.schoolmanagementapp.common.model.protocol.RoleAssignable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentEntity implements Serializable, RoleAssignable {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "school_user_id", referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne
    private SchoolClassEntity schoolClass;

    @OneToMany(mappedBy = "studentId")
    private Set<GradeEntity> grades;

    @Override
    public Set<RoleEntity> getRoles() {
        return this.user.getRoles();
    }
}
