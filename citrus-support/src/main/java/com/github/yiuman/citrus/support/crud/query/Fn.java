package com.github.yiuman.citrus.support.crud.query;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 可序列化的FunctionalInterface
 *
 * @author yiuman
 * @date 2021/8/20
 */
@FunctionalInterface
public interface Fn<T, R> extends Function<T, R>, Serializable {
}