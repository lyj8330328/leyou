package com.leyou.service;

import java.util.Map;

/**
 * @Author: 98050
 * Time: 2018-10-17 19:27
 * Feature:商品详情页后台
 */
public interface GoodsService {
    /**
     * 商品详细信息
     * @param spuId
     * @return
     */
    Map<String,Object> loadModel(Long spuId);
}
