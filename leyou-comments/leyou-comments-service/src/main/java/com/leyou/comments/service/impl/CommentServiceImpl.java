package com.leyou.comments.service.impl;
import com.leyou.comments.bo.CommentRequestParam;
import com.leyou.comments.client.OrderClient;
import com.leyou.comments.dao.CommentDao;
import com.leyou.comments.pojo.Review;
import com.leyou.comments.service.CommentService;
import com.leyou.utils.IdWorker;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author: 98050
 * @Time: 2018-11-26 15:41
 * @Feature:
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private OrderClient orderClient;


    @Override
    public Review findOne(String id) {
        //判断空
        Optional<Review> optional = commentDao.findById(id);
        return optional.orElse(null);
    }

    /**
     * 新增
     * 注意：一个用户只能发表一个顶级评论，可以追评（在一个订单中）
     * @param review
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(Long orderId,Review review) {
        //1.检查用户是否在该商品下发表过顶级评论过
        if (review.getIsparent()) {
            Query query2 = new Query();
            query2.addCriteria(Criteria.where("userid").is(review.getUserid()));
            query2.addCriteria(Criteria.where("orderid").is(review.getOrderid()));
            query2.addCriteria(Criteria.where("spuid").is(review.getSpuid()));
            List<Review> old = this.mongoTemplate.find(query2, Review.class);
            if (old.size() > 0 && old.get(0).getIsparent()) {
                return false;
            }
        }
        //2.修改订单状态,6代表评价状态
        boolean result = this.orderClient.updateOrderStatus(orderId, 6).getBody();
        if (!result){
            return false;
        }
        //3.添加评论
        /**
         * 设置主键
         */
        review.set_id(idWorker.nextId() + "");
        review.setPublishtime(new Date());
        review.setComment(0);
        review.setThumbup(0);
        review.setVisits(0);
        if (review.getParentid() != null && !"".equals(review.getParentid())){
            //如果存在上级id，则上级评论数加1，将上级评论的isParent设置为true，浏览量加一
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(review.getParentid()));
            Update update = new Update();
            update.inc("comment",1);
            update.set("isparent",true);
            update.inc("visits",1);
            this.mongoTemplate.updateFirst(query,update,"review");
        }
        commentDao.save(review);
        return true;
    }

    /**
     * 修改
     *
     * @param review
     */
    @Override
    public void update(Review review) {
        commentDao.save(review);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void deleteById(String id) {
        commentDao.deleteById(id);
    }

    /**
     * 查询某一商品下的所有顶级评论
     * @param commentRequestParam
     * @return
     */
    @Override
    public Page<Review> findReviewBySpuId(CommentRequestParam commentRequestParam) {
        PageRequest pageRequest = PageRequest.of(commentRequestParam.getPage()-1, commentRequestParam.getDefaultSize());
        return this.commentDao.findReviewBySpuid(commentRequestParam.getSpuId()+"",pageRequest);
    }

    /**
     * 评论点赞(需要改进)
     * @param id
     */
    @Override
    public boolean updateThumbup(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.inc("thumbup",1);
        UpdateResult result = this.mongoTemplate.updateFirst(query,update,"review");
        return result.isModifiedCountAvailable();
    }

    /**
     * 访问量加一
     * @param id
     */
    @Override
    public boolean updateVisits(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.inc("visits",1);
        UpdateResult result = this.mongoTemplate.updateFirst(query,update,"review");
        return result.isModifiedCountAvailable();
    }
}

