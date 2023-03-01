package com.example.springboot.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

import com.example.springboot.service.IBookService;
import com.example.springboot.entity.Book;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author WuSai
 * @since 2022-04-19
 */
@RestController
@RequestMapping("/book")
public class BookController {

    @Resource
    private IBookService bookService;

    @PostMapping
    public boolean save(@RequestBody Book book) {
        return bookService.saveOrUpdate(book); //table.entityPath == user
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return bookService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return bookService.removeByIds(ids);
    }

    @GetMapping
    public List<Book> findAll() {
        return bookService.list();
    }

    @GetMapping("/{id}")
    public Book findOne(@PathVariable Integer id) {
        return bookService.getById(id);
    }

    @GetMapping("/category")
    public List<String> getCategory(){
        return Constants.CATEGORY;
    }

    @GetMapping("/page")
    public Page<Book> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");

        return bookService.page(new Page<>(pageNum, pageSize), queryWrapper);
    }
}

