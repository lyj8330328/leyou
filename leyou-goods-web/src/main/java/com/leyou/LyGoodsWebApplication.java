package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author: 98050
 * Time: 2018-10-17 11:10
 * Feature: 商品详情微服务启动器，开启fegin功能
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LyGoodsWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyGoodsWebApplication.class,args);
    }
}
