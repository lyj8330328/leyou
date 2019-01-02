package com.leyou.item.service;

import com.leyou.item.pojo.Category;
import com.leyou.myexception.MyException;

import java.util.List;

/**
 * @Author: 98050
 * Time: 2018-08-07 19:16
 * Feature: 分类的业务层
 */
public interface CategoryService {

    /**
     * 根据id查询分类
     * @param pid
     * @return
     */
    List<Category> queryCategoryByPid(Long pid) throws MyException;

    /**
     * 根据brand id查询分类信息
     * @param bid
     * @return
     */
    List<Category> queryByBrandId(Long bid);

    /**
     * 保存
     * @param category
     */
    void saveCategory(Category category);

    /**
     * 更新
     * @param category
     */
    void updateCategory(Category category);

    /**
     * 删除
     * @param id
     */
    void deleteCategory(Long id);

    /**
     * 根据ids查询分类信息
     * @param asList
     * @return
     */
    List<String> queryNameByIds(List<Long> asList);

    /**
     * 查询当前数据库中最后一条数据
     * @return
     */
    List<Category> queryLast();

    /**
     * 根据分类id集合查询分类信息
     * @param ids
     * @return
     */
    List<Category> queryCategoryByIds(List<Long> ids);

    /**
     * 根据cid3查询其所有层级分类
     * @param id
     * @return
     */
    List<Category> queryAllCategoryLevelByCid3(Long id);
}
