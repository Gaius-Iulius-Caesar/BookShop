package com.example.springboot.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.controller.dto.GoodsDTO;
import com.example.springboot.controller.dto.InventoryItemDTO;
import com.example.springboot.entity.Book;
import com.example.springboot.entity.Shop;
import com.example.springboot.entity.User;
import com.example.springboot.service.IBookService;
import com.example.springboot.service.IShopService;
import com.example.springboot.service.IUserService;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.springboot.service.IInventoryService;
import com.example.springboot.entity.Inventory;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author WuSai
 * @since 2022-04-19
 */
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Resource
    private IInventoryService inventoryService;
    @Resource
    private IBookService bookService;
    @Resource
    private IUserService userService;

    @PostMapping
    public boolean save(@RequestBody Inventory inventory) {
        // 如果没有book信息，那么就在book表中新增并返回id，保存到inventory中
        // 确保每一条inventory都是有bookId的
        if (inventory.getBookId() == null) {
            Book book = new Book();
            book.setNull();
            bookService.save(book);
            inventory.setBookId(book.getId());
        }
        return inventoryService.saveOrUpdate(inventory); //table.entityPath == user
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return inventoryService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return inventoryService.removeByIds(ids);
    }

    @GetMapping
    public List<Inventory> findAll() {
        return inventoryService.list();
    }

    @GetMapping("/{id}")
    public Inventory findOne(@PathVariable Integer id) {
        return inventoryService.getById(id);
    }

    @GetMapping("/page")
    public Map<String, Object> findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam Integer shopId,
                                        @RequestParam(defaultValue = "") String bookname) {
        QueryWrapper<Inventory> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        queryWrapper.eq("shop_id", shopId);
        Page<Inventory> res = inventoryService.page(new Page<>(pageNum, pageSize), queryWrapper);
        // 获得原始数据
        List<Inventory> records = res.getRecords();
        long total = res.getTotal();
        // 按DTO类重写
        List<InventoryItemDTO> newRecords = new ArrayList<InventoryItemDTO>();
        for (Inventory row : records) {
            // 根据bookId获取book对象（注意: save已确保每一条inventory都是有bookId的）
            Integer bookId = row.getBookId();
            Book book = bookService.getById(bookId);
            // 不符合书名要求的书跳过
            if ((!bookname.isEmpty()) && (!book.getBookname().contains(bookname)))
                continue;
            // 设置DTO
            InventoryItemDTO inventoryItemDTO = new InventoryItemDTO();
            BeanUtil.copyProperties(row, inventoryItemDTO);
            inventoryItemDTO.setBookname(book.getBookname());
            inventoryItemDTO.setCategory(book.getCategory());
            // 将DTO加入列表
            newRecords.add(inventoryItemDTO);
        }
        Map<String, Object> newRes = new HashMap<>();
        newRes.put("records", newRecords);
        newRes.put("total", total);
        return newRes;
    }

    @GetMapping("/goods")
    public List<GoodsDTO> getAllGoods(@RequestParam String bookname,
                                      @RequestParam String category) {
        QueryWrapper<Inventory> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        queryWrapper.eq("enable", 1);
        // 获得原始数据
        List<Inventory> records = inventoryService.list(queryWrapper);
        // 按DTO类重写
        List<GoodsDTO> newRecords = new ArrayList<GoodsDTO>();
        for (Inventory row : records) {
            // 根据bookId获取book对象（注意: save已确保每一条inventory都是有bookId的）
            Integer bookId = row.getBookId();
            Book book = bookService.getById(bookId);
            Integer shopId = row.getShopId();
            User user = userService.getById(shopId);
            // 不符合书名要求的书跳过
            if ((!bookname.isEmpty()) && (!book.getBookname().contains(bookname)))
                continue;
            // 不符合分类要求的书跳过（受限于前端，分类值为0也表示无分类要求）
            if ((!category.isEmpty()) && !category.equals("0") && !book.getCategory().equals(category))
                continue;
            // 设置DTO
            GoodsDTO goodsDTO = new GoodsDTO();
            // 由于id字段冲突，注意复制顺序
            BeanUtil.copyProperties(book, goodsDTO);
            BeanUtil.copyProperties(row, goodsDTO);
            goodsDTO.setShopNickname(user.getNickname());
            // 将DTO加入列表
            newRecords.add(goodsDTO);
        }
        return newRecords;
    }

    /**
     * 使用hutool导出excel
     * 导出接口
     */
    @GetMapping("/export/{shopId}")
    public void export(HttpServletResponse response, @PathVariable Integer shopId) throws Exception {
        // 从数据库查询出所有的数据
        QueryWrapper<Inventory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_id", shopId);
        List<Inventory> list = inventoryService.list(queryWrapper);
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 修改list
        List<InventoryItemDTO> newList = new ArrayList<InventoryItemDTO>();
        for (Inventory row : list) {
            // 根据bookId获取book对象（注意: save已确保每一条inventory都是有bookId的）
            Integer bookId = row.getBookId();
            Book book = bookService.getById(bookId);
            // 设置DTO
            InventoryItemDTO inventoryItemDTO = new InventoryItemDTO();
            BeanUtil.copyProperties(row, inventoryItemDTO);
            inventoryItemDTO.setBookname(book.getBookname());
            inventoryItemDTO.setCategory(book.getCategory());
            // 将DTO加入列表
            newList.add(inventoryItemDTO);
        }
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("id", "库存编号");
        writer.addHeaderAlias("shopId", "商家编号");
        writer.addHeaderAlias("bookId", "图书编号");
        writer.addHeaderAlias("bookname", "书名");
        writer.addHeaderAlias("category", "分类");
        writer.addHeaderAlias("stock", "库存量");
        writer.addHeaderAlias("price", "价格");
        writer.addHeaderAlias("discount", "折扣");
        writer.addHeaderAlias("enable", "是否上架");
        //writer.addHeaderAlias("avatarUrl", "头像");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(newList, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("库存信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }
}

