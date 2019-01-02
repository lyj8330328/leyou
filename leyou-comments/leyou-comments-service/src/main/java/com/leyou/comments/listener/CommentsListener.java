package com.leyou.comments.listener;

import com.leyou.comments.dao.CommentDao;
import com.leyou.comments.pojo.Review;
import com.leyou.comments.service.CommentService;
import com.leyou.order.vo.CommentsParameter;
import com.leyou.utils.IdWorker;
import com.leyou.utils.JsonUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: 98050
 * @Time: 2018-12-12 11:54
 * @Feature:
 */
@Component
public class CommentsListener {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CommentDao commentDao;

    /**
     * 取到消息队列中信息，发布评论
     * @param string
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "leyou.comments.queue",durable = "true"), //队列持久化
            exchange = @Exchange(
                    value = "leyou.comments.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"user.comments"}
    ))
    public void listenCommentsMessage(String string){
        CommentsParameter commentsParameter = JsonUtils.parse(string, CommentsParameter.class);
        if (commentsParameter == null){
            return;
        }
        Review review = commentsParameter.getReview();

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
    }
}
