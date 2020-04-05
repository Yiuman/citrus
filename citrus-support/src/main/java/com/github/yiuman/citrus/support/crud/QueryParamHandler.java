package com.github.yiuman.citrus.support.crud;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;

/**
 * 查询参数处理器
 *
 * @author yiuman
 * @date 2020/4/5
 */
public interface QueryParamHandler {

    void handle(QueryParam queryParam, Object object, Field field, QueryWrapper<?> queryWrapper) throws Exception;
}