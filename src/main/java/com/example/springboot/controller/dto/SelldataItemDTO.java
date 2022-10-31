package com.example.springboot.controller.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 提供给/manage/selldata/records
 * 管理订单记录
 */
@Data
public class SelldataItemDTO {
    private Integer id;

    private Integer shopId;

    private Integer buyId;

    private Integer bookId;

    private Double price;

    private Integer count;

    private Integer isPay;

    private Integer isSent;

    private String expressId;

    private String address;

    private String connection;

    private String name;

    private LocalDateTime createTime;

    private String buyNickname;

    private String bookname;

}
