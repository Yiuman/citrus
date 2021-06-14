package com.github.yiuman.citrus.support.crud.query;

import com.github.yiuman.citrus.support.crud.query.annotations.QueryParam;
import lombok.Builder;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 查询参数元数据，用于记录某个参数实例的字段以及使用的查询方式
 * <p>
 * 配合@QueryParam注解使用，解析相关注解，构造响应的QueryParamMeta作下步处理
 *
 * @author yiuman
 * @date 2021/6/14
 * @see QueryParam
 */
@Data
@Builder
public class QueryParamMeta {

    /**
     * 元类型
     */
    private Class<?> metaClass;

    /**
     * 使用了注解的字段
     */
    private Field field;

    /**
     * 查询注解
     */
    private Annotation annotation;

    /**
     * 查询操作类型，如`=`、`like`、`in`等
     */
    private String type;

    /**
     * 条件为空时，是否拼接SQL
     */
    private boolean condition;

    /**
     * 映射名称，如实体的字段是helloWord，数据库映射的是hello_word
     */
    private String mapping;

    /**
     * 处理类
     */
    private Class<? extends QueryParamHandler> handlerClass;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QueryParamMeta)) {
            return false;
        }

        QueryParamMeta that = (QueryParamMeta) o;

        if (!Objects.equals(metaClass, that.metaClass)) {
            return false;
        }
        if (!Objects.equals(field, that.field)) {
            return false;
        }
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        int result = metaClass != null ? metaClass.hashCode() : 0;
        result = 31 * result + (field != null ? field.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
