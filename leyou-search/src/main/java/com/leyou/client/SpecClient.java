package com.leyou.client;

import com.leyou.item.api.SpecApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: 98050
 * Time: 2018-10-11 20:50
 * Feature:特有属性FeignClient
 */
@FeignClient(value = "item-service")
public interface SpecClient extends SpecApi {
}
