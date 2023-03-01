package com.example.springboot.controller.dto;

import lombok.Data;

/**
 * 提供给/store/storemain
 * 浏览商品信息
 */
@Data
public class GoodsDTO {
    private Integer id;

    private Integer shopId;

    private Integer bookId;

    private Integer stock;

    private Double price;

    private Double discount;

    private String bookname;

    private String author;

    private String introduction;

    private String press;

    private String category;

    private String coverurl;

    private String shopNickname;
}
