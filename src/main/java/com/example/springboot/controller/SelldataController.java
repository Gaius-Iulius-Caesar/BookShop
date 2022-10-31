package com.example.springboot.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.controller.dto.OrderDTO;
import com.example.springboot.controller.dto.SelldataItemDTO;
import com.example.springboot.entity.Book;
import com.example.springboot.entity.User;
import com.example.springboot.service.IBookService;
import com.example.springboot.service.IUserService;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.springboot.service.ISelldataService;
import com.example.springboot.entity.Selldata;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author WuSai
 * @since 2022-04-20
 */
@RestController
@RequestMapping("/selldata")
public class SelldataController {

    @Resource
    private ISelldataService selldataService;
    @Resource
    private IBookService bookService;
    @Resource
    private IUserService userService;

    // 特别为后台修改快递单号优化
    @PostMapping
    public boolean save(@RequestBody Selldata selldata) {
        // 如果物流编号非空，则认为已发货
        if(!selldata.getExpressId().isEmpty())
            selldata.setIsSent(1);
        else
            selldata.setIsSent(0);
        return selldataService.saveOrUpdate(selldata); //table.entityPath == user
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return selldataService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return selldataService.removeByIds(ids);
    }

    @GetMapping
    public List<Selldata> findAll() {
        return selldataService.list();
    }

    @GetMapping("/{id}")
    public Selldata findOne(@PathVariable Integer id) {
        return selldataService.getById(id);
    }

    // 为后台管理提供分页数据
    @GetMapping("/page")
    public Map<String, Object> findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam Integer shopId) {
        QueryWrapper<Selldata> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        queryWrapper.eq("shop_id", shopId);
        Page<Selldata> res = selldataService.page(new Page<>(pageNum, pageSize), queryWrapper);
        // 获得原始数据
        List<Selldata> records = res.getRecords();
        long total = res.getTotal();
        // 按DTO类重写
        List<SelldataItemDTO> newRecords = new ArrayList<SelldataItemDTO>();
        for (Selldata row : records) {
            // 根据bookId获取book对象
            Integer bookId = row.getBookId();
            Book book = bookService.getById(bookId);
            // 根据buyId获取user对象
            Integer buyId = row.getBuyId();
            User user = userService.getById(buyId);
            // 设置DTO
            SelldataItemDTO selldataItemDTO = new SelldataItemDTO();
            BeanUtil.copyProperties(row, selldataItemDTO);
            selldataItemDTO.setBookname(book.getBookname());
            selldataItemDTO.setBuyNickname(user.getNickname());
            // 将DTO加入列表
            newRecords.add(selldataItemDTO);
        }
        Map<String, Object> newRes = new HashMap<>();
        newRes.put("records", newRecords);
        newRes.put("total", total);
        return newRes;
    }

    // 为商品历史订单页面提供数据
    @GetMapping("/orders/{buyId}")
    public List<OrderDTO> getAllOrders(@PathVariable Integer buyId){
        // 获得当前用户的所有历史订单
        QueryWrapper<Selldata> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("buy_id", buyId);
        List<Selldata> records = selldataService.list(queryWrapper);
        // 按DTO类重写
        List<OrderDTO> newRecords = new ArrayList<OrderDTO>();
        for (Selldata row : records) {
            // 根据bookId获取book对象
            Integer bookId = row.getBookId();
            Book book = bookService.getById(bookId);
            // 根据buyId获取user对象
            Integer shopId = row.getShopId();
            User user = userService.getById(buyId);
            // 设置DTO
            OrderDTO orderDTO = new OrderDTO();
            // 由于id字段冲突，注意复制顺序
            BeanUtil.copyProperties(book, orderDTO);
            BeanUtil.copyProperties(row, orderDTO);
            orderDTO.setShopNickname(user.getNickname());
            // 将DTO加入列表
            newRecords.add(orderDTO);
        }
        return newRecords;
    }
}

