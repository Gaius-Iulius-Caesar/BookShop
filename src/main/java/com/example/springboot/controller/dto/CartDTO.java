package com.example.springboot.controller.dto;

import lombok.Data;

@Data
public class CartDTO {
    private Integer id;

    private Integer buyId;

    private Integer inventoryId;

    private Integer count;

    private String address;

    private String connection;

    private String name;

    private Integer bookId;

    private String bookname;

    private String coverurl;

    private Double newPrice;

    private Integer stock;

}
