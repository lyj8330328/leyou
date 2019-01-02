package com.leyou.auth.client;

import com.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: 98050
 * @Time: 2018-10-23 23:48
 * @Feature: 用户feignclient
 */
@FeignClient(value = "user-service")
public interface UserClient extends UserApi {
}
