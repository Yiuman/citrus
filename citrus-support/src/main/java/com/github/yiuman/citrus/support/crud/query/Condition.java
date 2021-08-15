package com.github.yiuman.citrus.support.crud.query;

import javax.servlet.http.HttpServletRequest;


/**
 * 查询条件接口
 *
 * @author yiuman
 * @date 2020/7/23
 */
public interface Condition {

    /**
     * 根据当前请求获取查询条件Wrapper
     *
     * @param request 当前请求
     * @return 查询条件QueryWrapper
     * @throws Exception 可能为反射异常
     */
    Query getQueryCondition(HttpServletRequest request) throws Exception;

    /**
     * 根据当前请求获取请求参数实例
     *
     * @param request 当前请求
     * @return 请求查询实例
     * @throws Exception 一般为反射异常
     */
    Object getQueryParams(HttpServletRequest request) throws Exception;

}