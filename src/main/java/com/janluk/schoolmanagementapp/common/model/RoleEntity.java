package com.janluk.schoolmanagementapp.common.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "roles")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "role")
public class RoleEntity implements Serializable {

    @Id
    private String role;
}
