package com.leyou.order.vo;

import com.leyou.comments.pojo.Review;

/**
 * @Author: 98050
 * @Time: 2018-12-12 11:43
 * @Feature: 新增评论消息对象
 */
public class CommentsParameter {

    private Long orderId;

    private Review review;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
