package com.github.yiuman.citrus.support.crud;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 基础CRUD服务类
 *
 * @author yiuman
 * @date 2020/4/4
 */
public interface CrudService<T, K> {

    /**
     * 保存前做的操作
     *
     * @param entity 当前实体
     */
    default void beforeSave(T entity) throws Exception {
    }

    /**
     * 保存
     *
     * @param entity 实体
     * @return 主键
     */
    @Transactional
    K saveEntity(T entity) throws Exception;

    /**
     * 保存后做的操作
     *
     * @param entity 当前实体
     */
    default void afterSave(T entity) throws Exception {
    }

    /**
     * 删除
     *
     * @param key 主键
     */
    @Transactional
    void delete(K key) throws Exception;

    /**
     * 根据主键查询
     *
     * @param key 主键
     * @return 实体
     */
    T get(K key) throws Exception;

    /**
     * 列表
     *
     * @return 实体列表
     */
    List<T> getList() throws Exception;

    List<T> getList(Wrapper<T> queryWrapper) throws Exception;

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    <P extends IPage<T>> P selectPage(P page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    void batchSave(Collection<T> entityList) throws Exception;

}