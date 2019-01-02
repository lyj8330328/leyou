package com.leyou.user.service;

import com.leyou.user.pojo.User;

/**
 * @Author: 98050
 * @Time: 2018-10-21 18:41
 * @Feature:
 */
public interface UserService {
    /**
     * 检查用户名和手机号是否可用
     * @param data
     * @param type
     * @return
     */
    Boolean checkData(String data, Integer type);

    /**
     * 发送手机验证码
     * @param phone
     * @return
     */
    Boolean sendVerifyCode(String phone);

    /**
     * 用户注册
     * @param user
     * @param code
     * @return
     */
    Boolean register(User user, String code);

    /**
     * 用户验证
     * @param username
     * @param password
     * @return
     */
    User queryUser(String username, String password);

    /**
     * 根据用户名修改密码
     * @param username
     * @param newPassword
     * @return
     */
    boolean updatePassword(String username,String oldPassword,String newPassword);
}
