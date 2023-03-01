package com.example.springboot.controller.dto;

import lombok.Data;

/**
 * 提供给/manage/inventory
 * 管理库存记录
 */
@Data
public class InventoryItemDTO {
    private Integer id;

    private Integer shopId;

    private Integer bookId;

    private Integer stock;

    private Integer price;

    private Double discount;

    private Integer enable;

    private String bookname;

    private String category;
}
