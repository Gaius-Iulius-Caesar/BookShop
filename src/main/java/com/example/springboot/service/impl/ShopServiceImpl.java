package com.example.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.ShopDTO;
import com.example.springboot.entity.Shop;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.ShopMapper;
import com.example.springboot.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.utils.TokenUtils;
import org.apache.commons.lang.SerializationException;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author WuSai
 * @since 2022-04-05
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

    @Override
    public ShopDTO login(ShopDTO shopDTO) {
        QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", shopDTO.getUsername());
        queryWrapper.eq("password", shopDTO.getPassword());
        Shop one;
        try{
            one = getOne(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        if(one != null) {
            BeanUtil.copyProperties(one, shopDTO, true);
            // 设置jwt-token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            shopDTO.setToken(token);
            return shopDTO;
        }else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    @Override
    public Shop register(ShopDTO shopDTO) {
        QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", shopDTO.getUsername());
        Shop one;
        try{
            one = getOne(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        if(one == null) {
            one = new Shop();
            BeanUtil.copyProperties(shopDTO, one, true);
            save(one);
        }else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return one;
    }
}
