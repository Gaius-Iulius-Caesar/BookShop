package com.example.springboot.mapper;

import com.example.springboot.entity.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author WuSai
 * @since 2022-04-19
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {

}
