package com.github.yiuman.citrus.support.crud.rest;

import com.github.yiuman.citrus.support.crud.query.Condition;
import com.github.yiuman.citrus.support.model.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 查询Restful接口定义
 *
 * @author yiuman
 * @date 2020/10/1
 */
public interface QueryRestful<T, K> extends Condition {

    /**
     * 分页
     *
     * @param request 当前请求
     * @return 分页实体
     * @throws Exception 如转化参数的异常、反射异常等
     */
    Page<T> page(HttpServletRequest request) throws Exception;


    /**
     * 根据主键获取实体
     *
     * @param key 主键
     * @return 实体
     * @throws Exception 数据库操作异常等
     */
    T get(K key) throws Exception;


    /**
     * 导出
     *
     * @param request  当前请求
     * @param response 当前响应
     * @throws Exception IO异常等
     */
    void exp(HttpServletRequest request, HttpServletResponse response) throws Exception;
}