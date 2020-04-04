package com.github.yiuman.citrus.support.wrapper;

/**
 * 消费wrapper
 * @author yiuman
 * @date 2020/4/3
 */
public interface ConsumerWrapper<T, E extends Exception> {

    void accept(T t) throws E;
}

