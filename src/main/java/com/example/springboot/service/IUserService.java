package com.example.springboot.service;

import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author WuSai
 * @since 2022-04-19
 */
public interface IUserService extends IService<User> {
    UserDTO login(UserDTO userDTO);

    void register(UserDTO userDTO);
}
