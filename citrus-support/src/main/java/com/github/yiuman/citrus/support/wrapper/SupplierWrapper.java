package com.github.yiuman.citrus.support.wrapper;

/**
 * 可抛异常的Supplier
 *
 * @author yiuman
 * @date 2020/4/6
 */
public interface SupplierWrapper<T, E extends Exception> {

    /**
     * 工厂方法
     *
     * @return 获取对象
     * @throws E 抛出的异常
     */
    T get() throws E;
}