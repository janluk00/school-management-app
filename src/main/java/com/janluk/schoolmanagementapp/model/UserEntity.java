package com.janluk.schoolmanagementapp.model;

import com.janluk.schoolmanagementapp.model.vo.BirthDate;
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
public class UserEntity implements Serializable {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private BirthDate birthDate;

    @Column(nullable = false)
    private String token;

    @ManyToMany
    @JoinTable(name="school_users_roles",
            joinColumns=
            @JoinColumn(name="school_user_id", referencedColumnName="id"),
            inverseJoinColumns=
            @JoinColumn(name="role", referencedColumnName="role")
    )
    @Builder.Default
    private Set<RoleEntity> roles = new HashSet<>();
}
