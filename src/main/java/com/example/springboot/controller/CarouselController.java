package com.example.springboot.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.CarouselItemDTO;
import com.example.springboot.entity.User;
import com.example.springboot.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.springboot.service.ICarouselService;
import com.example.springboot.entity.Carousel;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author WuSai
 * @since 2022-04-22
 */
@RestController
@RequestMapping("/carousel")
public class CarouselController {

    @Resource
    private ICarouselService carouselService;
    @Resource
    private IUserService userService;

    @PostMapping
    public boolean save(@RequestBody Carousel carousel) {
        return carouselService.saveOrUpdate(carousel); //table.entityPath == user
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return carouselService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return carouselService.removeByIds(ids);
    }

    @GetMapping
    public List<Carousel> findAll() {
        return carouselService.list();
    }

    @GetMapping("/{id}")
    public Carousel findOne(@PathVariable Integer id) {
        return carouselService.getById(id);
    }

    @GetMapping("/page")
    public Map<String, Object> findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam(defaultValue = "") String imageInfo) {
        QueryWrapper<Carousel> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        if (!imageInfo.isEmpty())
            queryWrapper.like("image_info", imageInfo);
        Page<Carousel> res = carouselService.page(new Page<>(pageNum, pageSize), queryWrapper);
        // 获得原始数据
        List<Carousel> records = res.getRecords();
        long total = res.getTotal();
        // 按DTO类重写
        List<CarouselItemDTO> newRecords = new ArrayList<CarouselItemDTO>();
        for (Carousel row : records) {
            // 根据bookId获取book对象（注意: save已确保每一条inventory都是有bookId的）
            Integer adminId = row.getAdminId();
            User user = userService.getById(adminId);
            // 设置DTO
            CarouselItemDTO carouselItemDTO = new CarouselItemDTO();
            BeanUtil.copyProperties(row, carouselItemDTO);
            carouselItemDTO.setAdminNickname(user.getNickname());
            // 将DTO加入列表
            newRecords.add(carouselItemDTO);
        }
        Map<String, Object> newRes = new HashMap<>();
        newRes.put("records", newRecords);
        newRes.put("total", total);
        return newRes;
    }

    @GetMapping("/play")
    public Result getPlayData(){
        QueryWrapper<Carousel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enable", 1);
        return Result.success(carouselService.list(queryWrapper));
    }
}

