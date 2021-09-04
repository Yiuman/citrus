package com.github.yiuman.citrus.support.crud.query;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 可序列化的FunctionalInterface
 *
 * @param <T> 实体类型
 * @param <R> 返回类型
 * @author yiuman
 * @date 2021/8/20
 */
@FunctionalInterface
public interface Fn<T, R> extends Function<T, R>, Serializable {
}