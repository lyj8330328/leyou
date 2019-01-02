package com.leyou.user.api;

import com.leyou.user.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: 98050
 * @Time: 2018-10-23 23:50
 * @Feature: 用户服务接口
 */
public interface UserApi {
    /**
     * 用户验证
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    User queryUser(@RequestParam("username")String username, @RequestParam("password")String password);
}
