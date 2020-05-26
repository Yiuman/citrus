package com.github.yiuman.citrus.support.utils;

import com.google.common.collect.Maps;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yiuman
 * @date 2020/4/6
 */
public final class ConvertUtils {

    private ConvertUtils() {
    }

    public static <S, T> T convert(Class<T> clazz, S source) throws Exception {
        T t = clazz.newInstance();
        org.springframework.beans.BeanUtils.copyProperties(source, t);
        return t;
    }

    public static <S, T> List<T> listConvert(Class<T> clazz, Collection<S> source) {
        return source.stream().map(LambdaUtils.functionWrapper(item -> convert(clazz, item))).collect(Collectors.toList());
    }

    /**
     * 对象转Map
     *
     * @param s   对象
     * @param <S> 类型
     */
    public static <S> Map<String, Object> objectToMap(S s) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Map<String, Object> map = Maps.newHashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(s.getClass());
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            if ("class".equals(pd.getName())) {
                continue;
            }
            Object invokeValue = pd.getReadMethod().invoke(s);
            map.put(pd.getDisplayName(), invokeValue == null ? "" : invokeValue);
        }
        return map;
    }
}
