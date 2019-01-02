package com.leyou.order.vo;

/**
 * @Author: 98050
 * @Time: 2018-12-10 23:27
 * @Feature:
 */
public class OrderStatusMessage {
    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 用户id
     */
    private Long userId;

    private String username;

    private Long spuId;

    /**
     * 消息类型：1(自动确认收货)  2（自动评论）
     */
    private int type;


    public OrderStatusMessage() {
    }

    public OrderStatusMessage(Long orderId, Long userId, String username, Long spuId, int type) {
        this.orderId = orderId;
        this.userId = userId;
        this.username = username;
        this.spuId = spuId;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "OrderStatusMessage{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", spuId=" + spuId +
                ", type=" + type +
                '}';
    }
}
