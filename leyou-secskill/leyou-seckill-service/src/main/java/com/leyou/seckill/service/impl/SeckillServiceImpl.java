package com.leyou.seckill.service.impl;

import com.leyou.item.pojo.SeckillGoods;
import com.leyou.item.pojo.Stock;
import com.leyou.order.pojo.Order;
import com.leyou.order.pojo.OrderDetail;
import com.leyou.order.pojo.SeckillOrder;
import com.leyou.seckill.client.GoodsClient;
import com.leyou.seckill.client.OrderClient;
import com.leyou.seckill.mapper.SeckillMapper;
import com.leyou.seckill.mapper.SeckillOrderMapper;
import com.leyou.seckill.mapper.SkuMapper;
import com.leyou.seckill.mapper.StockMapper;
import com.leyou.seckill.service.SeckillService;
import com.leyou.seckill.vo.SeckillMessage;
import com.leyou.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 98050
 * @Time: 2018-11-10 16:57
 * @Feature:
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private SeckillMapper seckillMapper;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    private static final Logger LOGGER = LoggerFactory.getLogger(SeckillServiceImpl.class);

    private static final String KEY_PREFIX_PATH = "leyou:seckill:path";


    /**
     * 创建订单
     * @param seckillGoods
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrder(SeckillGoods seckillGoods) {

        Order order = new Order();
        order.setPaymentType(1);
        order.setTotalPay(seckillGoods.getSeckillPrice());
        order.setActualPay(seckillGoods.getSeckillPrice());
        order.setPostFee(0+"");
        order.setReceiver("李四");
        order.setReceiverMobile("15812312312");
        order.setReceiverCity("西安");
        order.setReceiverDistrict("碑林区");
        order.setReceiverState("陕西");
        order.setReceiverZip("000000000");
        order.setInvoiceType(0);
        order.setSourceType(2);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setSkuId(seckillGoods.getSkuId());
        orderDetail.setNum(1);
        orderDetail.setTitle(seckillGoods.getTitle());
        orderDetail.setImage(seckillGoods.getImage());
        orderDetail.setPrice(seckillGoods.getSeckillPrice());
        orderDetail.setOwnSpec(this.skuMapper.selectByPrimaryKey(seckillGoods.getSkuId()).getOwnSpec());

        order.setOrderDetails(Arrays.asList(orderDetail));


        String seck = "seckill";
        ResponseEntity<List<Long>> responseEntity = this.orderClient.createOrder(seck,order);

        if (responseEntity.getStatusCode() == HttpStatus.OK){
            //库存不足，返回null
            return null;
        }
        return responseEntity.getBody().get(0);
    }

    /**
     * 检查秒杀库存
     * @param skuId
     * @return
     */
    @Override
    public boolean queryStock(Long skuId) {
        Stock stock = this.stockMapper.selectByPrimaryKey(skuId);
        if (stock.getSeckillStock() - 1 < 0){
            return false;
        }
        return true;
    }

    /**
     * 发送消息到秒杀队列当中
     * @param seckillMessage
     */
    @Override
    public void sendMessage(SeckillMessage seckillMessage) {
        String json = JsonUtils.serialize(seckillMessage);
        try {
            this.amqpTemplate.convertAndSend("order.seckill", json);
        }catch (Exception e){
            LOGGER.error("秒杀商品消息发送异常，商品id：{}",seckillMessage.getSeckillGoods().getSkuId(),e);
        }
    }

    /**
     * 根据用户id查询秒杀订单
     * @param userId
     * @return
     */
    @Override
    public Long checkSeckillOrder(Long userId) {
        Example example = new Example(SeckillOrder.class);
        example.createCriteria().andEqualTo("userId",userId);
        List<SeckillOrder> seckillOrders = this.seckillOrderMapper.selectByExample(example);
        if (seckillOrders == null || seckillOrders.size() == 0){
            return null;
        }
        return seckillOrders.get(0).getOrderId();
    }

    /**
     * 创建秒杀地址
     * @param goodsId
     * @param id
     * @return
     */
    @Override
    public String createPath(Long goodsId, Long id) {
        String str = new BCryptPasswordEncoder().encode(goodsId.toString()+id);
        BoundHashOperations<String,Object,Object> hashOperations = this.stringRedisTemplate.boundHashOps(KEY_PREFIX_PATH);
        String key = id.toString() + "_" + goodsId;
        hashOperations.put(key,str);
        hashOperations.expire(60, TimeUnit.SECONDS);
        return str;
    }

    /**
     * 验证秒杀地址
     * @param goodsId
     * @param id
     * @param path
     * @return
     */
    @Override
    public boolean checkSeckillPath(Long goodsId, Long id, String path) {
        String key = id.toString() + "_" + goodsId;
        BoundHashOperations<String,Object,Object> hashOperations = this.stringRedisTemplate.boundHashOps(KEY_PREFIX_PATH);
        String encodePath = (String) hashOperations.get(key);
        return new BCryptPasswordEncoder().matches(path,encodePath);
    }


}
