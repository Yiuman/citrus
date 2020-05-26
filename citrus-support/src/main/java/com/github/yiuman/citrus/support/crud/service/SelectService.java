package com.github.yiuman.citrus.support.crud.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 查询Service
 *
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
     * 获取全部数据列表
     *
     * @return List
     */
    List<E> list();

    /**
     * 根据查询条件获取对应的数据列表
     *
     * @param wrapper 查询条件
     * @return List
     */
    List<E> list(Wrapper<E> wrapper);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @return 实现了IPage接口的页面对象
     */
    <P extends IPage<E>> P page(P page, @Param(Constants.WRAPPER) Wrapper<E> queryWrapper);

}