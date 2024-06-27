package com.janluk.schoolmanagementapp.common.model;

import com.janluk.schoolmanagementapp.common.model.protocol.RoleAssignable;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "students")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentEntity implements Serializable, RoleAssignable {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "school_user_id", referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne
    private SchoolClassEntity schoolClass;

    @OneToMany(mappedBy = "studentId")
    @Builder.Default
    private List<GradeEntity> grades = new ArrayList<>();

    @Override
    public Set<RoleEntity> getRoles() {
        return this.user.getRoles();
    }
}
