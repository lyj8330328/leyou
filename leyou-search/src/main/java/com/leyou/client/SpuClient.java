package com.leyou.client;

import com.leyou.item.api.SpuApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: 98050
 * Time: 2018-10-11 20:50
 * Feature:spu FeignClient
 */
@FeignClient(value = "item-service")
public interface SpuClient extends SpuApi {
}
