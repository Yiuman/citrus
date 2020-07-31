package com.github.yiuman.citrus.support.utils;

import com.google.common.collect.Maps;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.ReflectionUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 转换工具类
 *
 * @author yiuman
 * @date 2020/4/6
 */
public final class ConvertUtils {

    private final static ExpressionParser expressionParser = new SpelExpressionParser();

    private ConvertUtils() {
    }

    public static <T> T parseEl(String el, Class<T> clazz) {
        return expressionParser.parseExpression(el).getValue(clazz);
    }

    public static Object parseEl(String el) {
        return expressionParser.parseExpression(el).getValue();
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

    /**
     * 枚举转ListMap
     *
     * @param enumClass 枚举类型
     * @param <T>       枚举
     * @return List-Map  Map中包括枚举类型的所有字段
     */
    public static <T extends Enum<?>> List<Map<String, ?>> enumToListMap(Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants()).map(enumObject -> {
            Map<String, Object> map = Maps.newHashMap();
            ReflectionUtils.doWithFields(enumClass, (field -> {
                field.setAccessible(true);
                map.put(field.getName(), field.get(enumObject));
            }));

            return map;
        }).collect(Collectors.toList());
    }

}
