package com.github.yiuman.citrus.support.utils;

import cn.hutool.core.convert.Convert;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.ReflectionUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 转换工具类
 *
 * @author yiuman
 * @date 2020/4/6
 */
public final class ConvertUtils {

    private static final  ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true)
            .setSerializationInclusion(JsonInclude.Include.ALWAYS);

    private ConvertUtils() {
    }

    public static <T> T parseEl(String el, Class<T> clazz) {
        return EXPRESSION_PARSER.parseExpression(el).getValue(clazz);
    }

    public static Object parseEl(String el) {
        return EXPRESSION_PARSER.parseExpression(el).getValue();
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
        BeanInfo beanInfo = Introspector.getBeanInfo(s.getClass());
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        Map<String, Object> map = new HashMap<>(pds.length);
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
            Map<String, Object> map = new HashMap<>(1);
            ReflectionUtils.doWithFields(enumClass, (field -> {
                field.setAccessible(true);
                map.put(field.getName(), field.get(enumObject));
            }));

            return map;
        }).collect(Collectors.toList());
    }

    /**
     * 将Map数据赋值到目前对象中
     *
     * @param target 目标对象
     * @param map    map数据集
     * @param <E>    目前 对象类型
     * @return 赋值完成的目标对象
     * @throws IntrospectionException 内省异常
     */
    public static <E> E mapAssignment(E target, Map<String, Object> map) throws IntrospectionException {
        Class<?> targetClass = ClassUtils.getRealClass(target.getClass());
        BeanInfo beanInfo = Introspector.getBeanInfo(targetClass);
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        Arrays.stream(pds).forEach(LambdaUtils.consumerWrapper(pd -> {
            Object writeValue = map.get(pd.getDisplayName());

            if (Objects.nonNull(writeValue)) {
                Method writeMethod = pd.getWriteMethod();
                writeMethod.setAccessible(true);
                writeMethod.invoke(target, Convert.convert(pd.getPropertyType(), writeValue));
            }
        }));
        return target;

    }

    public static <T> Map<?, ?> bean2JsonMap(T target) throws JsonProcessingException {
        String jsonString = OBJECT_MAPPER.writeValueAsString(target);
        return OBJECT_MAPPER.readValue(jsonString, Map.class);
    }


}
