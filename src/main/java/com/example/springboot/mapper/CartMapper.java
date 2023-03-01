package com.example.springboot.mapper;

import com.example.springboot.entity.Cart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author WuSai
 * @since 2022-04-24
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {

}