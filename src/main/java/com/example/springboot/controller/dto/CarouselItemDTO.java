package com.example.springboot.controller.dto;

import lombok.Data;

/**
 * 提供给/admin/admincarousel
 * 管理轮播图记录
 */
@Data
public class CarouselItemDTO {
    private Integer id;

    private Integer adminId;

    private String imageInfo;

    private Integer enable;

    private String imageurl;

    private String adminNickname;
}
