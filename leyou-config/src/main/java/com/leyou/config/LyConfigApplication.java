package com.leyou.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @Author: 98050
 * @Time: 2018-11-28 16:39
 * @Feature: 配置中心
 */
@EnableConfigServer
@SpringBootApplication
public class LyConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(LyConfigApplication.class,args);
    }
}
