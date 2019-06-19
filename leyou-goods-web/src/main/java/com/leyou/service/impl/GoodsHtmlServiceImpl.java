package com.leyou.service.impl;

import com.leyou.service.GoodsHtmlService;
import com.leyou.service.GoodsService;


import com.leyou.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @Author: 98050
 * @Time: 2018-10-19 09:46
 * @Feature: 实现页面静态化接口
 */
@Service
public class GoodsHtmlServiceImpl implements GoodsHtmlService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private TemplateEngine templateEngine;

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsHtmlService.class);


    @Override
    public void createHtml(Long spuId) throws InterruptedException, ExecutionException {
        PrintWriter writer = null;

        //获取页面数据
        Map<String,Object> spuMap = this.goodsService.loadModel(spuId);
        //创建Thymeleaf上下文对象
        Context context = new Context();
        //把数据放入上下文对象
        context.setVariables(spuMap);

        //创建输出流
        File file = new File("D:\\nginx-1.12.2\\html\\item\\"+spuId+".html");
        try {
            writer = new PrintWriter(file);
            //执行页面静态化方法
            templateEngine.process("item",context,writer);
        } catch (FileNotFoundException e) {
            LOGGER.error("页面静态化出错：{}"+e,spuId);
        }finally {
            if (writer != null){
                writer.close();
            }
        }
    }

    /**
     * 新建线程处理页面静态化
     * @param spuId
     */
    @Override
    public void asyncExecute(Long spuId) {
        ThreadUtils.execute(() -> {
            try {
                createHtml(spuId);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void deleteHtml(Long id) {
        File file = new File("D:\\nginx-1.12.2\\html\\item\\"+id+".html");
        file.deleteOnExit();
    }
}
