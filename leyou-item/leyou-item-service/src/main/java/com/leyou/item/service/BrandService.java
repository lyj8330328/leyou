package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.parameter.pojo.BrandQueryByPageParameter;

import java.util.List;

/**
 * @Author: 98050
 * Time: 2018-08-07 19:16
 * Feature: 分类的业务层
 */

public interface BrandService {

    /**
     * 分页查询
     * @param brandQueryByPageParameter
     * @return
     */
    PageResult<Brand> queryBrandByPage(BrandQueryByPageParameter brandQueryByPageParameter);

    /**
     * 新增brand,并且维护中间表
     * @param brand
     * @param cids
     */
    void saveBrand(Brand brand, List<Long> cids);

    /**
     * 修改brand，并且维护中间表
     * @param brand
     * @param cids
     */
    void updateBrand(Brand brand, List<Long> cids);

    /**
     * 删除brand，并且维护中间表
     * @param id
     */
    void deleteBrand(Long id);


    /**
     * 根据brand Id 删除中间表中的数据
     * @param bid
     */
    void deleteByBrandIdInCategoryBrand(Long bid);

    /**
     * 根据category id查询brand
     * @param cid
     * @return
     */
    List<Brand> queryBrandByCategoryId(Long cid);

    /**
     * 根据品牌id集合查询品牌信息
     * @param ids
     * @return
     */
    List<Brand> queryBrandByBrandIds(List<Long> ids);
}
