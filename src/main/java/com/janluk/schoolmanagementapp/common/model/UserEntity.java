package com.janluk.schoolmanagementapp.common.model;

import com.janluk.schoolmanagementapp.common.model.protocol.RoleAssignable;
import com.janluk.schoolmanagementapp.common.model.vo.BirthDate;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "school_users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements Serializable, RoleAssignable {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String email;

    @Column
    private String password;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private BirthDate birthDate;

    @Column
    private String passwordConfirmationToken;

    @ManyToMany
    @JoinTable(name="school_users_roles",
            joinColumns=
            @JoinColumn(name="school_user_id", referencedColumnName="id"),
            inverseJoinColumns=
            @JoinColumn(name="role", referencedColumnName="role")
    )
    @Builder.Default
    private Set<RoleEntity> roles = new HashSet<>();

    @Override
    public UserEntity getUser() {
        return this;
    }
}
