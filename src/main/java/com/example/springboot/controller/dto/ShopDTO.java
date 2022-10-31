package com.example.springboot.controller.dto;

import lombok.Data;

/*
 * 接收前端商家登录请求
 */
@Data
public class ShopDTO {
    private String username;
    private String password;
    private String shopname;
    private String pictureurl;
    private String token;
}
