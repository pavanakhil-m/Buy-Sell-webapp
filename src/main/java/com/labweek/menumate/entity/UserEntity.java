package com.labweek.menumate.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table( name = "appusers")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ntid", nullable = false)
    private String ntId;

    @Column(name = "username", nullable = false)
    private String userName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phoneno", nullable = false)
    private String phoneNo;

    @Column(name = "password", nullable = false)
    private String password;

//    @Column(name = "image_url")
//    private String imageUrl;

//    @Column(name = "login")
//    private String login;

}
