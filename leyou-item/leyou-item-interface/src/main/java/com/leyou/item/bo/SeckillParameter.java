package com.leyou.item.bo;


/**
 * @Author: 98050
 * @Time: 2018-11-10 17:30
 * @Feature: 秒杀设置参数
 */
public class SeckillParameter {

    /**
     * 要秒杀的sku id
     */
    private Long id;

    /**
     * 秒杀开始时间
     */
    private String startTime;

    /**
     * 秒杀结束时间
     */
    private String endTime;

    /**
     * 参与秒杀的商品数量
     */
    private Integer count;

    /**
     * 折扣
     */
    private double  discount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "SeckillParameter{" +
                "id=" + id +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", count=" + count +
                ", discount=" + discount +
                '}';
    }
}
