package com.leyou.comments.dao;


import com.leyou.comments.pojo.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


/**
 * @Author: 98050
 * @Time: 2018-11-26 20:51
 * @Feature:
 */
public interface CommentDao extends MongoRepository<Review,String> {

    /**
     * 分页查询
     * @param spuId
     * @param pageable
     * @return
     */
    Page<Review> findReviewBySpuid(String spuId, Pageable pageable);
}
