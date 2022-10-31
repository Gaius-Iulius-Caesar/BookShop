package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author WuSai
 * @since 2022-04-19
 */
@Getter
@Setter
  @TableName("sys_book")
@ApiModel(value = "Book对象", description = "")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private String bookname;

    private String author;

    private String introduction;

    private String press;

    private String category;

    private String coverurl;

    public void setNull(){
        this.id = null;
        this.bookname = "未知书目";
        this.author = null;
        this.introduction = null;
        this.press = null;
        this.category = null;
        this.coverurl = null;
    }

}
