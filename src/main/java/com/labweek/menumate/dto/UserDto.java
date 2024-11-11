package com.labweek.menumate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {

    private Long id;
    private String ntId;
    private String email;
    private String phoneNo;
    private String userName;
    private String token;




//    private String login;
//    private String imageUrl;


}
