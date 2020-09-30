package com.github.yiuman.citrus.support.crud.rest;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * CrudRestful接口 用于定义Rest的操作
 *
 * @author yiuman
 * @date 2020/5/8
 */
public interface CrudRestful<T, K> extends QueryRestful<T, K> {

    /**
     * 保存实体
     *
     * @param entity 当前实体
     * @return 实体主键
     * @throws Exception 数据库操作异常等
     */
    K save(T entity) throws Exception;

    /**
     * 根据主键删除记录
     *
     * @param key 主键
     * @return true/false true代表删除成功，false失败
     * @throws Exception 数据库操作异常
     */
    Boolean delete(K key) throws Exception;

    /**
     * 批量删除
     *
     * @param keys 主键集合
     * @throws Exception 数据库操作异常等
     */
    void batchDelete(List<K> keys) throws Exception;

    /**
     * 导入文件
     *
     * @param file 文件
     * @throws Exception IO异常等
     */
    void imp(MultipartFile file) throws Exception;

}