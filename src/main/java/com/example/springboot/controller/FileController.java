package com.example.springboot.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Files;
import com.example.springboot.mapper.FileMapper;
import lombok.SneakyThrows;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 文件控制器
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private FileMapper fileMapper;

    /**
     * 文件上传接口
     * 使用md5解决了磁盘数据冗余问题
     * @param file 前端传递过来的文件
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();
        // ================存储到磁盘=================
        File uploadParentFile = new File(fileUploadPath);
        // 若文件目录不存在，则创建
        if (!uploadParentFile.exists()) {
            uploadParentFile.mkdirs();
        }

        // 获取文件的md5，根据文件存在与否获取url
        String md5 = SecureUtil.md5(file.getInputStream());
        String url;
        // 从数据库查询是否存在相同的记录（自定义方法）
        Files dbFiles = getFileByMd5(md5);
        if (dbFiles != null) {
            // 存在相同记录
            // 不存盘，直接将已有记录的fileurl赋值给url
            url = dbFiles.getFileurl();
        } else {
            // 不存在相同记录
            // 依据唯一标识码存储至磁盘目录
            String uuid = IdUtil.fastSimpleUUID();
            String fileUUID = uuid + StrUtil.DOT + type;
            File uploadFile = new File(fileUploadPath + fileUUID);
            file.transferTo(uploadFile);
            // 数据库若不存在相同文件，则将新fileurl传给url
            url = "http://localhost:9090/file/" + fileUUID;
        }

        // ==========存储到数据库=========
        Files saveFile = new Files();
        saveFile.setFilename(originalFilename);
        saveFile.setFiletype(type);
        saveFile.setFilesize(size / 1024); // 单位 kb
        saveFile.setFileurl(url);
        saveFile.setMd5(md5);
        fileMapper.insert(saveFile);

        return url;
    }

    /**
     * 文件下载接口   http://localhost:9090/file/{fileUUID}
     *
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");

        // 读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    /**
     * 通过文件的md5查询文件
     * @param md5
     * @return
     */
    private Files getFileByMd5(String md5) {
        // 查询文件的md5是否存在
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> filesList = fileMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }
}
