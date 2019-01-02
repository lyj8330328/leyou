package com.leyou.auth.service;

/**
 * @Author: 98050
 * @Time: 2018-10-23 22:46
 * @Feature:
 */
public interface AuthService {
    /**
     * 用户授权
     * @param username
     * @param password
     * @return
     */
    String authentication(String username, String password);
}
