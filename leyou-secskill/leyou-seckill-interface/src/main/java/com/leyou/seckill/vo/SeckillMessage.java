package com.leyou.seckill.vo;

import com.leyou.auth.entity.UserInfo;
import com.leyou.item.pojo.SeckillGoods;

/**
 * @Author: 98050
 * @Time: 2018-11-15 20:19
 * @Feature: 秒杀信息
 */
public class SeckillMessage {
    /**
     * 用户信息
     */
    private UserInfo userInfo;

    /**
     * 秒杀商品
     */
    private SeckillGoods seckillGoods;

    public SeckillMessage() {
    }

    public SeckillMessage(UserInfo userInfo, SeckillGoods seckillGoods) {
        this.userInfo = userInfo;
        this.seckillGoods = seckillGoods;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public SeckillGoods getSeckillGoods() {
        return seckillGoods;
    }

    public void setSeckillGoods(SeckillGoods seckillGoods) {
        this.seckillGoods = seckillGoods;
    }
}
