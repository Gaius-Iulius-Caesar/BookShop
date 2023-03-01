package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author WuSai
 * @since 2022-04-20
 */
@Getter
@Setter
@TableName("sys_selldata")
@ApiModel(value = "Selldata对象", description = "")
public class Selldata implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
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

    private Date createTime;


}
