package com.janluk.schoolmanagementapp.model;

import com.janluk.schoolmanagementapp.model.vo.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity implements Serializable {

    @Id
    @Enumerated(EnumType.STRING)
    private RoleType role;
}
