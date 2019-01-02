package com.leyou.utils;

import java.util.concurrent.*;

/**
 * @Author: 98050
 * @Time: 2018-10-19 10:19
 * @Feature: 线程工具
 */
public class ThreadUtils {

    private static final ExecutorService es = Executors.newFixedThreadPool(10);

    public static void execute(Runnable runnable){
        es.submit(runnable);
    }
}
