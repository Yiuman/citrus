package com.github.yiuman.citrus.support.wrapper;

/**
 * @author yiuman
 * @date 2020/4/6
 */
public interface SupplierWrapper<T,E extends Exception> {

    T get() throws E;
}