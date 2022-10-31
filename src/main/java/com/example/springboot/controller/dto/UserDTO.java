package com.example.springboot.controller.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatarurl;
    private Integer role;
    private String token;
}
