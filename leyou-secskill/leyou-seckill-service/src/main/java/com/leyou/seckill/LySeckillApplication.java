package com.leyou.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author: 98050
 * @Time: 2018-11-10 10:57
 * @Feature:
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("com.leyou.seckill.mapper")
public class LySeckillApplication {
    public static void main(String[] args) {
        SpringApplication.run(LySeckillApplication.class,args);
    }
}
