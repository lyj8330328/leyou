package com.leyou.client;

import com.leyou.item.api.SpuApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: 98050
 * Time: 2018-10-17 19:02
 * Feature:
 */
@FeignClient(value = "item-service")
public interface SpuClient extends SpuApi {
}
