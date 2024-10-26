package com.janluk.schoolmanagementapp.common.model;

import com.janluk.schoolmanagementapp.common.model.protocol.RoleAssignable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static com.janluk.schoolmanagementapp.common.user.TokenGenerator.*;

@Entity
@Table(name = "school_users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity implements Serializable, RoleAssignable {

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
    private LocalDate birthDate;

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

    public void updatePasswordAndClearToken(String password) {
        this.password = password;
        this.passwordConfirmationToken = null;
    }

    public void generateNewToken() {
        this.passwordConfirmationToken = generateToken();
    }
}
