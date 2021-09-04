package com.github.yiuman.citrus.support.crud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.yiuman.citrus.support.crud.query.Query;

import java.util.List;

/**
 * 查询Service
 *
 * @param <E> 实体类型
 * @param <K> 主键类型
 * @author yiuman
 * @date 2020/4/15
 */
public interface SelectService<E, K> {

    /**
     * 根据主键获取实体
     *
     * @param key 主键
     * @return 实体
     */
    E get(K key);

    /**
     * 根据条件查询单个
     *
     * @param query 查询条件
     * @return 单个实体
     */
    E get(Query query);

    /**
     * 获取全部数据列表
     *
     * @return List
     */
    List<E> list();

    /**
     * 根据查询条件获取对应的数据列表
     *
     * @param query 查询条件
     * @return List
     */
    List<E> list(Query query);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     *
     * @param page  分页查询条件（可以为 RowBounds.DEFAULT）
     * @param query 实体对象封装操作类（可以为 null）
     * @return 实现了IPage接口的页面对象
     */
    <P extends IPage<E>> P page(P page, Query query);

}