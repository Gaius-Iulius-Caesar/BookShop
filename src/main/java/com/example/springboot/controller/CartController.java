package com.example.springboot.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.CartDTO;
import com.example.springboot.entity.*;
import com.example.springboot.service.IBookService;
import com.example.springboot.service.IInventoryService;
import com.example.springboot.service.ISelldataService;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import com.example.springboot.service.ICartService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author WuSai
 * @since 2022-04-24
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Resource
    private ICartService cartService;
    @Resource
    private IInventoryService inventoryService;
    @Resource
    private IBookService bookService;
    @Resource
    private ISelldataService selldataService;

    @PostMapping
    public boolean save(@RequestBody Cart cart) {
        if(cart.getId() == null){
            // 商品主页点击加入购物车
            QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("buy_id", cart.getBuyId());
            queryWrapper.eq("inventory_id", cart.getInventoryId());
            Cart res = cartService.getOne(queryWrapper);
            if(res == null){
                // 购物车没有此商品
                cart.setCount(1);
                return cartService.saveOrUpdate(cart);
            }
            else {
                // 购物车已有此商品
                res.setCount(res.getCount()+1);
                return cartService.saveOrUpdate(res);
            }
        }
        else {
            // 购物车和订单页面修改信息
            return cartService.saveOrUpdate(cart);
        }
    }
    @PostMapping("/goods-receive")
    public Result saveGoodsReceiveInfo(@RequestBody Cart cart) {
        UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<Cart>();
        updateWrapper.eq("buy_id", cart.getBuyId());
        updateWrapper.set("address", cart.getAddress());
        updateWrapper.set("connection", cart.getConnection());
        updateWrapper.set("name", cart.getName());
        if(cartService.update(null, updateWrapper))
            return Result.success();
        else
            return Result.error(Constants.CODE_600, "更新收货信息失败");
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return cartService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return cartService.removeByIds(ids);
    }

    @GetMapping
    public List<Cart> findAll() {
        return cartService.list();
    }

    @GetMapping("/{buyId}")
    public List<CartDTO> getCartDataById(@PathVariable Integer buyId) {
        // 获得当前用户全部购物车数据
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("buy_id", buyId);
        List<Cart> records = cartService.list(queryWrapper);
        // 按DTO类重写
        List<CartDTO> newRecords = new ArrayList<CartDTO>();
        for (Cart row : records) {
            // 根据inventoryId获取inventory对象
            Integer inventoryId = row.getInventoryId();
            Inventory inventory = inventoryService.getById(inventoryId);
            // 根据bookId获取book对象
            Integer bookId = inventory.getBookId();
            Book book = bookService.getById(bookId);
            // 设置DTO
            CartDTO cartDTO = new CartDTO();
            BeanUtil.copyProperties(row, cartDTO);
            cartDTO.setBookId(bookId);
            cartDTO.setBookname(book.getBookname());
            cartDTO.setCoverurl(book.getCoverurl());
            cartDTO.setNewPrice(inventory.getPrice() * inventory.getDiscount());
            cartDTO.setStock(inventory.getStock());
            // 将DTO加入列表
            newRecords.add(cartDTO);
        }
        return newRecords;
    }

    @GetMapping("pay-success/{buyId}")
    public Result paySuccess(@PathVariable Integer buyId){
        // 获得当前用户全部购物车数据
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("buy_id", buyId);
        List<Cart> records = cartService.list(queryWrapper);
        // 把每一条购物车数据转化为销售数据
        for (Cart row : records) {
            // 得到每一条数据对应的库存
            Inventory inventory = inventoryService.getById(row.getInventoryId());
            // 设置销售数据
            Selldata selldata = new Selldata();
            BeanUtil.copyProperties(row, selldata);
            selldata.setId(null); // 插入必须保持id为null
            selldata.setIsPay(1); // 默认已支付成功
            selldata.setShopId(inventory.getShopId());
            selldata.setBookId(inventory.getBookId());
            selldata.setPrice(inventory.getPrice() * inventory.getDiscount());
            selldataService.save(selldata);
        }
        // 清空购物车
        for (Cart row : records) {
           cartService.removeById(row.getId());
        }
        return Result.success();
    }

    @GetMapping("/page")
    public Page<Cart> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");

        return cartService.page(new Page<>(pageNum, pageSize), queryWrapper);
    }
}

