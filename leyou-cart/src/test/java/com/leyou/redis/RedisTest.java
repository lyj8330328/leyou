//package com.leyou.redis;
//
//import com.leyou.cart.LyCartApplication;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.BoundHashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
///**
// * @Author: 98050
// * @Time: 2018-10-25 22:58
// * @Feature:
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = LyCartApplication.class)
//public class RedisTest {
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Autowired
//    private RedisTemplate<String,String> redisTemplate;
//
//    @Test
//    public void test(){
//
//        BoundHashOperations<String,String,String> boundHashOperations = this.redisTemplate.boundHashOps("test");
////        boundHashOperations.put("1", "test1");
////        boundHashOperations.put("2", "test2");
////        boundHashOperations.put("3", "test3");
//        List<String> result = boundHashOperations.values();
//        for (Object o : result){
//            System.out.println(o);
//        }
//    }
//
//    @Test
//    public void test2(){
//
//        BoundHashOperations<String,String,String> boundHashOperations = this.redisTemplate.boundHashOps("test");
////        boundHashOperations.put("1", "test1");
////        boundHashOperations.put("2", "test2");
////        boundHashOperations.put("3", "test3");
//        List<String> result = boundHashOperations.values();
//        for (Object o : result){
//            System.out.println(o);
//        }
//    }
//}
