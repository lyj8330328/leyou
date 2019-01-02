package com.leyou.comments.controller;

import com.leyou.auth.entity.UserInfo;
import com.leyou.comments.bo.CommentRequestParam;
import com.leyou.comments.interceptor.LoginInterceptor;
import com.leyou.comments.pojo.Review;
import com.leyou.comments.service.CommentService;
import com.leyou.common.pojo.PageResult;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 98050
 * @Time: 2018-11-26 21:30
 * @Feature:
 */
@RequestMapping
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private RedisTemplate redisTemplate;

    private final String THUMBUP_PREFIX = "thumbup";

    /**
     * 分页查询某一商品下的所有顶级评论
     * @param requestParam
     * @return
     */
    @GetMapping("list")
    public ResponseEntity findReviewBySpuId(@RequestBody CommentRequestParam requestParam){
        Page<Review> result = commentService.findReviewBySpuId(requestParam);
        if (result == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        PageResult pageResult = new PageResult();
        pageResult.setTotal(result.getTotalElements());
        pageResult.setItems(result.getContent());
        pageResult.setTotalPage((long)result.getTotalPages());
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 评论点赞
     * @param id
     * @return
     */
    @PutMapping("thumb/{id}")
    public ResponseEntity<Boolean> updateThumbup(@PathVariable String id){

        //1.首先判断当前用户是否点过赞
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        String userId = userInfo.getId()+"";
        if (redisTemplate.opsForValue().get(THUMBUP_PREFIX + userId + "_" + id) != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        boolean result = this.commentService.updateThumbup(id);
        if (!result){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        redisTemplate.opsForValue().set(THUMBUP_PREFIX + userId + "_" + id,"1");
        return ResponseEntity.ok(result);
    }

    /**
     * 增加评论
     * @param review
     * @return
     */
    @PostMapping("comment/{orderId}")
    public ResponseEntity<Void> addReview(@PathVariable("orderId") Long orderId,@RequestBody Review review){
        boolean result = this.commentService.add(orderId,review);
        if (!result){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据评论id查询评论
     * @param id
     * @return
     */
    @GetMapping("/commentId/{id}")
    public ResponseEntity<Review> findReviewById(@PathVariable("id") String id){
        Review review = this.commentService.findOne(id);
        if (review == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(review);
    }

    /**
     * 修改评论
     * @param review
     * @return
     */
    @PutMapping("/comment")
    public ResponseEntity<Void> updateReview(@RequestBody Review review){
        this.commentService.update(review);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 根据评论id删除评论
     * @param id
     * @return
     */
    @DeleteMapping("/commentId/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable("id") String id){
        this.commentService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 根据评论id访问量加1
     * @param id
     * @return
     */
    @PutMapping("visit/{id}")
    public ResponseEntity<Void> updateReviewVisit(@PathVariable("id") String id){
        boolean result = this.commentService.updateVisits(id);
        if (!result){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
