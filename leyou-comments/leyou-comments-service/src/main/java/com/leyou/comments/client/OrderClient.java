package com.leyou.comments.client;

import com.leyou.order.api.OrderApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: 98050
 * @Time: 2018-11-12 15:19
 * @Feature: 订单接口
 */
@FeignClient(value = "order-service")
public interface OrderClient extends OrderApi {
}
