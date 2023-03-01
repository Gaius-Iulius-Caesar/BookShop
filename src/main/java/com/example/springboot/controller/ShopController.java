package com.example.springboot.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.ShopDTO;
import com.example.springboot.entity.Shop;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.IShopService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author WuSai
 * @since 2022-04-05
 */
@RestController
@RequestMapping("/shop")
public class ShopController {
    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private IShopService shopService;

    @PostMapping("/login")
    public Result login(@RequestBody ShopDTO shopDTO) {
        String username = shopDTO.getUsername();
        String password = shopDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        ShopDTO dto = shopService.login(shopDTO);
        return Result.success(dto);
    }

    @PostMapping("/register")
    public Result register(@RequestBody ShopDTO shopDTO) {
        String username = shopDTO.getUsername();
        String password = shopDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        Shop shop = shopService.register(shopDTO);
        return Result.success(shop);
    }

    @PostMapping
    public boolean save(@RequestBody Shop shop) {
        // 不使用shopService.saveOrUpdate(shop)
        // return shopService.saveOrUpdate(shop); //table.entityPath == shop
        // 因为该方法只有在主键为空时更新
        // 但我们某种意义上是用username区分商家的
        UpdateWrapper<Shop> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("username", shop.getUsername());
        if(!shop.getShopname().isEmpty())
            updateWrapper.set("shopname", shop.getShopname());
        if(!shop.getEmail().isEmpty())
            updateWrapper.set("email", shop.getEmail());
        if(!shop.getPhone().isEmpty())
            updateWrapper.set("phone", shop.getPhone());
        if(!shop.getAddress().isEmpty())
            updateWrapper.set("address", shop.getAddress());
        return shopService.update(null, updateWrapper);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return shopService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return shopService.removeByIds(ids);
    }

    @GetMapping
    public List<Shop> findAll() {
        return shopService.list();
    }

//    @GetMapping("/{id}")
//    public Shop findOne(@PathVariable Integer id) {
//        return shopService.getById(id);
//    }

    /**
     * 根据用户名获取商店信息
     */
    @GetMapping("/{username}")
    public Result getShopInfo(@PathVariable String username) {
        QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Shop one;
        try {
            one = shopService.getOne(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        if (one != null) {
            return Result.success(one);
        } else {
            throw new ServiceException(Constants.CODE_600, "不存在此商家信息");
        }
    }

    @GetMapping("/page")
    public Page<Shop> findPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam(defaultValue = "") String username,
                               @RequestParam(defaultValue = "") String email,
                               @RequestParam(defaultValue = "") String address) {
        QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        if (!username.isEmpty())
            queryWrapper.like("username", username);
        if (!email.isEmpty())
            queryWrapper.like("email", email);
        return shopService.page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    /**
     * 前端上传图片后，FileController会返回url
     * 前端利用此方法传递该url，以更新shop
     * 存储pictureurl并再次返回给前端
     */
    @GetMapping("/save-picture")
    public Result saveAndGetPictureUrl(@RequestParam String username, @RequestParam String pictureurl) {
        UpdateWrapper<Shop> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("username", username);
        updateWrapper.set("pictureurl", pictureurl);
        shopService.update(null, updateWrapper);
        return Result.success(pictureurl);
    }

    @GetMapping("/change-password")
    public Result changePassword(@RequestParam String username,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword) {
        QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Shop one = shopService.getOne(queryWrapper);
        if (one.getPassword().equals(oldPassword)) {
            UpdateWrapper<Shop> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("username", username);
            updateWrapper.set("password", newPassword);
            if (shopService.update(null, updateWrapper))
                return Result.success();
            else
                return Result.error();
        } else {
            return Result.error();
        }
    }
}

