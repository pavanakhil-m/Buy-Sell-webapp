package com.labweek.menumate.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="superadmin")

public class SuperAdminEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
            (name = "ntid",
            nullable = false)
    private String Nt_id;

    @Column
            (name = "name")
    private String SuperAdminName;

    @Column
            (name = "email")
    private String SuperAdminEmail;

    @Column
            (name = "password")
    private String SuperAdminPassword;

}
