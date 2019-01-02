package com.leyou.repository;

import com.leyou.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author: 98050
 * Time: 2018-10-11 22:17
 * Feature:文档操作
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
