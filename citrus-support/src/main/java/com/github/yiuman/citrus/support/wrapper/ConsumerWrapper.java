package com.github.yiuman.citrus.support.wrapper;

/**
 * 可抛异常的消费wrapper
 *
 * @param <T> 消费入参类型
 * @param <E> 异常类型
 * @author yiuman
 * @date 2020/4/3
 */
public interface ConsumerWrapper<T, E extends Exception> {

    /**
     * 接收某个参数进行消费
     *
     * @param t 参数
     * @throws E 抛出的异常
     */
    void accept(T t) throws E;
}

