package com.github.yiuman.citrus.support.wrapper;

/**
 *
 * @author yiuman
 * @date 2020/4/3
 */
public interface FunctionWrapper<T,R,E extends Exception> {

    R apply(T t) throws E;
}