package com.leyou.order.pojo;



import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: 98050
 * @Time: 2018-10-27 16:34
 * @Feature: 订单状态实体类
 */
@Table(name = "tb_order_status")
public class OrderStatus {

    /**
     * 初始阶段：1、未付款、未发货；初始化所有数据
     * 付款阶段：2、已付款、未发货；更改付款时间
     * 发货阶段：3、已发货，未确认；更改发货时间、物流名称、物流单号
     * 成功阶段：4、已确认，未评价；更改交易结束时间
     * 关闭阶段：5、关闭； 更改更新时间，交易关闭时间。
     * 评价阶段：6、已评价
     */

    @Id
    private Long orderId;

    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 付款时间
     */
    private Date paymentTime;

    /**
     *  发货时间
     */
    private Date consignTime;

    /**
     * 交易结束时间
     */
    private Date endTime;

    /**
     * 交易关闭时间
     */
    private Date closeTime;

    /**
     * 评价时间
     */
    private Date commentTime;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getConsignTime() {
        return consignTime;
    }

    public void setConsignTime(Date consignTime) {
        this.consignTime = consignTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    @Override
    public String toString() {
        return "OrderStatus{" +
                "orderId=" + orderId +
                ", status=" + status +
                ", createTime=" + createTime +
                ", paymentTime=" + paymentTime +
                ", consignTime=" + consignTime +
                ", endTime=" + endTime +
                ", closeTime=" + closeTime +
                ", commentTime=" + commentTime +
                '}';
    }
}
