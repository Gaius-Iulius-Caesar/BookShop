package com.example.springboot.common;

import java.util.Arrays;
import java.util.List;

// 定义状态码常量
public interface Constants {
    String CODE_200 = "200"; // 成功
    String CODE_401 = "401"; // 权限不足
    String CODE_400 = "400"; // 参数错误
    String CODE_500 = "500"; // 系统错误
    String CODE_600 = "600"; // 其它业务异常

    List<String> CATEGORY = Arrays.asList("文学作品", "医学生理", "商业经济", "科学技术", "实用工具", "自然地理", "其它");
}
