package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_file")
public class Files {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String filename;
    private String filetype;
    private Long filesize;
    private String fileurl;
    private String md5;
    private Boolean isDelete;
    private Boolean enable;

}
