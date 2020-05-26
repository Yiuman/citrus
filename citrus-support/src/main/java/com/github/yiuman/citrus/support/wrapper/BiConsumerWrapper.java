package com.github.yiuman.citrus.support.wrapper;

/**
 * BiConsumer接口的扩展，用于抛异常
 *
 * @author yiuman
 * @date 2020/4/3
 */
public interface BiConsumerWrapper<T, U, E extends Exception> {

    /**
     * 接受两个参数并处理
     *
     * @param t 参数1
     * @param u 参数2
     * @throws E 抛出的异常
     */
    void accept(T t, U u) throws E;
}