package com.leyou.item.mapper;

import com.leyou.item.pojo.Stock;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author: 98050
 * Time: 2018-08-17 16:10
 * Feature:
 */
@org.apache.ibatis.annotations.Mapper
public interface StockMapper extends Mapper<Stock>, SelectByIdListMapper<Stock,Long> {
}
