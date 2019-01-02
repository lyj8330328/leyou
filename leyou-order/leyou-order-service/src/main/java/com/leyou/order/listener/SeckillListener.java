package com.leyou.order.listener;

import com.leyou.auth.entity.UserInfo;
import com.leyou.item.pojo.SeckillGoods;
import com.leyou.item.pojo.Stock;
import com.leyou.order.mapper.*;
import com.leyou.order.pojo.Order;
import com.leyou.order.pojo.OrderDetail;
import com.leyou.order.pojo.OrderStatus;
import com.leyou.order.pojo.SeckillOrder;
import com.leyou.order.service.OrderService;
import com.leyou.seckill.vo.SeckillMessage;
import com.leyou.utils.IdWorker;
import com.leyou.utils.JsonUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author: 98050
 * @Time: 2018-11-15 20:30
 * @Feature: 秒杀消息队列监听器
 */
@Component
public class SeckillListener {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private SeckillMapper seckillMapper;

    /**
     * 接收秒杀信息
     * @param seck
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "leyou.order.seckill.queue",durable = "true"), //队列持久化
            exchange = @Exchange(
                    value = "leyou.order.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"order.seckill"}
    ))
    @Transactional(rollbackFor = Exception.class)
    public void listenSeckill(String seck){

        SeckillMessage seckillMessage = JsonUtils.parse(seck,SeckillMessage.class);
        UserInfo userInfo = seckillMessage.getUserInfo();
        SeckillGoods seckillGoods = seckillMessage.getSeckillGoods();


        //1.首先判断库存是否充足
        Stock stock = stockMapper.selectByPrimaryKey(seckillGoods.getSkuId());
        if (stock.getSeckillStock() <= 0 || stock.getStock() <= 0){
            //如果库存不足的话修改秒杀商品的enable字段
            Example example = new Example(SeckillGoods.class);
            example.createCriteria().andEqualTo("skuId", seckillGoods.getSkuId());
            List<SeckillGoods> list = this.seckillMapper.selectByExample(example);
            for (SeckillGoods temp : list){
                if (temp.getEnable()){
                    temp.setEnable(false);
                    this.seckillMapper.updateByPrimaryKeySelective(temp);
                }
            }
            return;
        }
        //2.判断此用户是否已经秒杀到了
        Example example = new Example(SeckillOrder.class);
        example.createCriteria().andEqualTo("userId",userInfo.getId()).andEqualTo("skuId",seckillGoods.getSkuId());
        List<SeckillOrder> list = this.seckillOrderMapper.selectByExample(example);
        if (list.size() > 0){
            return;
        }
        //3.下订单
        //构造order对象
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

        //3.1 生成orderId
        long orderId = idWorker.nextId();
        //3.2 初始化数据
        order.setBuyerNick(userInfo.getUsername());
        order.setBuyerRate(false);
        order.setCreateTime(new Date());
        order.setOrderId(orderId);
        order.setUserId(userInfo.getId());
        //3.3 保存数据
        this.orderMapper.insertSelective(order);

        //3.4 保存订单状态
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCreateTime(order.getCreateTime());
        //初始状态未未付款：1
        orderStatus.setStatus(1);
        //3.5 保存数据
        this.orderStatusMapper.insertSelective(orderStatus);

        //3.6 在订单详情中添加orderId
        order.getOrderDetails().forEach(od -> {
            //添加订单
            od.setOrderId(orderId);
        });

        //3.7 保存订单详情，使用批量插入功能
        this.orderDetailMapper.insertList(order.getOrderDetails());

        //3.8 修改库存
        order.getOrderDetails().forEach(ord -> {
            Stock stock1 = this.stockMapper.selectByPrimaryKey(ord.getSkuId());
            stock1.setStock(stock1.getStock() - ord.getNum());
            stock1.setSeckillStock(stock1.getSeckillStock() - ord.getNum());
            this.stockMapper.updateByPrimaryKeySelective(stock1);

            //新建秒杀订单
            SeckillOrder seckillOrder = new SeckillOrder();
            seckillOrder.setOrderId(orderId);
            seckillOrder.setSkuId(ord.getSkuId());
            seckillOrder.setUserId(userInfo.getId());
            this.seckillOrderMapper.insert(seckillOrder);

        });


    }
}
