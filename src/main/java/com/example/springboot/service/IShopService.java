package com.example.springboot.service;

import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.ShopDTO;
import com.example.springboot.entity.Shop;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author WuSai
 * @since 2022-04-05
 */
public interface IShopService extends IService<Shop> {

    ShopDTO login(ShopDTO shopDTO);

    Shop register(ShopDTO shopDTO);
}
