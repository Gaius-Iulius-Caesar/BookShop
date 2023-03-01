package com.example.springboot.controller.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 提供给/store/storeorder
 * 浏览历史订单
 */
@Data
public class OrderDTO {
    private Integer id;

    private Integer shopId;

    private Integer buyId;

    private Integer bookId;

    private Double price;

    private Integer count;

    private Integer isPay;

    private Integer isSent;

    private String expressId;

    private LocalDateTime createTime;

    private String bookname;

    private String author;

    private String introduction;

    private String press;

    private String category;

    private String coverurl;

    private String shopNickname;
}
