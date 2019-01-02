package com.leyou.seckill.service;


import com.leyou.item.pojo.SeckillGoods;
import com.leyou.seckill.vo.SeckillMessage;

/**
 * @Author: 98050
 * @Time: 2018-11-10 16:56
 * @Feature:
 */
public interface SeckillService {

    /**
     * 创建订单
     * @param seckillGoods
     * @return
     */
    Long createOrder(SeckillGoods seckillGoods);


    /**
     * 检查库存
     * @param skuId
     * @return
     */
    boolean queryStock(Long skuId);

    /**
     * 发送秒杀信息到队列当中
     * @param seckillMessage
     */
    void sendMessage(SeckillMessage seckillMessage);

    /**
     * 根据用户id查询秒杀订单
     * @param userId
     * @return
     */
    Long checkSeckillOrder(Long userId);


    /**
     * 创建秒杀地址
     * @param goodsId
     * @param id
     * @return
     */
    String createPath(Long goodsId, Long id);

    /**
     * 验证秒杀地址
     * @param goodsId
     * @param id
     * @param path
     * @return
     */
    boolean checkSeckillPath(Long goodsId, Long id, String path);

}
