package com.github.yiuman.citrus.support.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

/**
 * @author yiuman
 * @date 2020/7/23
 */
public final class ClassUtils {

    private ClassUtils() {
    }

    public static Class<?> getGenericInterfaceType(Class<?> clazz) {
        Type[] types = clazz.getGenericInterfaces();
        ParameterizedType parameterized = (ParameterizedType) types[0];
        return (Class<?>) parameterized.getActualTypeArguments()[0];
    }

    public static Class<?> getRealClass(Class<?> clazz) {
        if (Proxy.isProxyClass(clazz)) {
            return clazz.getInterfaces()[0];
        } else {
            return org.springframework.util.ClassUtils.getUserClass(clazz);
        }
    }

}
