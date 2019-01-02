package com.leyou.order.service;

import com.leyou.order.vo.CommentsParameter;
import com.leyou.order.vo.OrderStatusMessage;

/**
 * @Author: 98050
 * @Time: 2018-12-10 23:17
 * @Feature: 发送延时消息和评论消息
 */
public interface OrderStatusService {


    /**
     * 发送消息到延时队列
     * @param orderStatusMessage
     */
    void sendMessage(OrderStatusMessage orderStatusMessage);

    /**
     * 发送评论信息
     * @param commentsParameter
     */
    void sendComments(CommentsParameter commentsParameter);
}
