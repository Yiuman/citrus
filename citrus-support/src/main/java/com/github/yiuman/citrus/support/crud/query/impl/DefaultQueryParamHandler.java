package com.github.yiuman.citrus.support.crud.query.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yiuman.citrus.support.crud.query.Query;
import com.github.yiuman.citrus.support.crud.query.QueryParamHandler;
import com.github.yiuman.citrus.support.crud.query.QueryParamMeta;
import com.github.yiuman.citrus.support.crud.query.builder.QueryBuilders;
import com.github.yiuman.citrus.support.crud.query.builder.SimpleQueryBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;

/**
 * 默认的查询参数处理器
 *
 * @author yiuman
 * @date 2021/6/14
 */
public class DefaultQueryParamHandler implements QueryParamHandler {

    public DefaultQueryParamHandler() {
    }

    @Override
    public void handle(QueryParamMeta paramMeta, Object object, Query query) throws Exception {
        Field field = paramMeta.getField();
        field.setAccessible(true);
        Object value = field.get(object);
        if (Objects.isNull(value)) {
            return;
        }
        SimpleQueryBuilder simpleQueryBuilder = QueryBuilders.wrapper(query);
        Method conditionMethod = simpleQueryBuilder
                .getClass()
                .getMethod(paramMeta.getOperator(), String.class, getParameterClass(field));
        conditionMethod.setAccessible(true);
        String fieldName = ObjectUtil.isNotEmpty(paramMeta.getMapping())
                ? paramMeta.getMapping()
                : field.getName();
        conditionMethod.invoke(simpleQueryBuilder, fieldName, field.get(object));
    }

    private Class<?> getParameterClass(Field field) {
        Class<?> fieldType = field.getType();
        if (fieldType.isArray()) {
            return Object[].class;
        }

        if (Collection.class.isAssignableFrom(fieldType)) {
            return Collection.class;
        }

        return Object.class;
    }
}
