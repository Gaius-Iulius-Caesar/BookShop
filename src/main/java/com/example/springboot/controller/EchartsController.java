package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.PieDTO;
import com.example.springboot.entity.Book;
import com.example.springboot.entity.Inventory;
import com.example.springboot.entity.Selldata;
import com.example.springboot.service.IBookService;
import com.example.springboot.service.ISelldataService;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/echarts")
public class EchartsController {
    @Resource
    private ISelldataService selldataService;
    @Resource
    private IBookService bookService;

    /**
     * 查询各图书种类销售数量
     * 返回饼图数据
     * 数据为List<pieDTO>
     */
    @GetMapping("/pie/{shopId}")
    public Result getPieData(@PathVariable Integer shopId) {
        // 获取该商家全部销售数据
        QueryWrapper<Selldata> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_id", shopId);
        List<Selldata> selldata = selldataService.list(queryWrapper);
        // 组装返回数据
        List<PieDTO> pieData = new ArrayList<>();
        for (String category : Constants.CATEGORY) {
            // 设置名称
            PieDTO pieDTO = new PieDTO();
            pieDTO.setName(category);
            // 设置一个变量记录数量
            int value = 0;
            // 对每一条销售记录，查询该记录是否为相应的图书种类
            for (Selldata row : selldata) {
                Integer bookId = row.getBookId();
                Book book = bookService.getById(bookId);
                if (book.getCategory().equals(category))
                    value += 1;
            }
            pieDTO.setValue(value);
            pieData.add(pieDTO);
        }
        return Result.success(pieData);
    }

    /**
     * 查询图书各月销售数量
     * 返回直线和柱状复合图数据
     * 数据为List<Integer>
     */
    @GetMapping("/lineandbar/{shopId}")
    private Result getLineAndBarData(@PathVariable Integer shopId) {
        // 获取该商家全部销售数据
        QueryWrapper<Selldata> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_id", shopId);
        List<Selldata> selldata = selldataService.list(queryWrapper);
        // 组装返回数据
        List<Integer> lineAndBarData = new ArrayList<>(Collections.nCopies(12, 0));
        for (Selldata row : selldata) {
            lineAndBarData.set(row.getCreateTime().getMonth(), lineAndBarData.get(row.getCreateTime().getMonth())+1);
        }
        return Result.success(lineAndBarData);
    }
}
