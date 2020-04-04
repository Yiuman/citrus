package com.github.yiuman.citrus.support.wrapper;

/**
 * @author yiuman
 * @date 2020/4/3
 */
public interface BiConsumerWrapper<T,U,E extends Exception> {

    void accept(T t, U u) throws E;
}