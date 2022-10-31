package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author WuSai
 * @since 2022-04-05
 */
@Getter
@Setter
@TableName("test_sys_shop")
@ApiModel(value = "Shop对象", description = "")
@ToString
public class Shop implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;
    @Ignore
    private String password;

    private String shopname;

    private String email;

    private String phone;

    private String address;

    private String pictureurl;


}
