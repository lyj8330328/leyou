package com.leyou.comments.pojo;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: 98050
 * @Time: 2018-11-26 14:45
 * @Feature:
 */
public class Review implements Serializable {

    @Id
    private String _id;

    /**
     * 订单id
     */
    private String orderid;
    /**
     * 商品id
     */
    private String spuid;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论时间
     */
    private Date publishtime;
    /**
     * 评论用户id
     */
    private String userid;
    /**
     * 评论用户昵称
     */
    private String nickname;
    /**
     * 评论的浏览量
     */
    private Integer visits;
    /**
     * 评论的点赞数
     */
    private Integer thumbup;
    /**
     * 评论中的图片
     */
    private List<String> images;
    /**
     * 评论的回复数
     */
    private Integer comment;
    /**
     * 该评论是否可以被回复
     */
    private Boolean iscomment;
    /**
     * 该评论的上一级id
     */
    private String parentid;
    /**
     * 是否是顶级评论
     */
    private Boolean isparent;
    /**
     * 评论的类型
     */
    private Integer type;

    /**
     * json转换需要
     */
    public Review() {
    }

    public Review(String orderid,String spuid, String content, String userid, String nickname, List<String> images, Boolean iscomment, String parentid, Boolean isparent, Integer type) {
        this.orderid = orderid;
        this.spuid = spuid;
        this.content = content;
        this.userid = userid;
        this.nickname = nickname;
        this.images = images;
        this.iscomment = iscomment;
        this.parentid = parentid;
        this.isparent = isparent;
        this.type = type;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSpuid() {
        return spuid;
    }

    public void setSpuid(String spuid) {
        this.spuid = spuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(Date publishtime) {
        this.publishtime = publishtime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }

    public Integer getThumbup() {
        return thumbup;
    }

    public void setThumbup(Integer thumbup) {
        this.thumbup = thumbup;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public Boolean getIscomment() {
        return iscomment;
    }

    public void setIscomment(Boolean comment) {
        this.iscomment = comment;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public Boolean getIsparent() {
        return isparent;
    }

    public void setIsparent(Boolean parent) {
        this.isparent = parent;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
