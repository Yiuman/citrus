package com.github.yiuman.citrus.support.crud.query;

/**
 * 查询参数处理器
 *
 * @author yiuman
 * @date 2020/4/5
 */
public interface QueryParamHandler {

    /**
     * 根据@QueryParam注解的定义构造查询条件
     *
     * @param paramMeta 查询参数元信息
     * @param object    当前查询参数对象
     * @param query     查询参数体
     * @throws Exception 大多为反射异常
     */
    void handle(QueryParamMeta paramMeta, Object object, Query query) throws Exception;
}