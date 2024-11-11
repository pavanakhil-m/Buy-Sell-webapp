package com.labweek.menumate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SignUpDto {

    private Long id;
    private String ntId;
    private String email;
    private String phoneNo;
    private String userName;
    private String password;


//    private String login;
//    private String imageUrl;
}
