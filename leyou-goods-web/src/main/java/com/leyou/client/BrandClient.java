package com.leyou.client;

/**
 * @Author: 98050
 * Time: 2018-10-17 18:59
 * Feature:品牌FeignClient
 */

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service")
public interface BrandClient extends BrandApi {
}
