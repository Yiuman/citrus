package com.github.yiuman.citrus.support.inject.impl;

import com.github.yiuman.citrus.support.inject.InjectAnnotationParser;
import com.github.yiuman.citrus.support.inject.InjectAnnotationParserHolder;
import com.github.yiuman.citrus.support.utils.ClassUtils;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * 注解注入解析器持有实现
 *
 * @author yiuman
 * @date 2020/7/23
 */
@Component
@SuppressWarnings("unchecked")
public class InjectAnnotationParserHolderImpl implements InjectAnnotationParserHolder {

    /**
     * 从容器中注入
     */
    private final Set<InjectAnnotationParser<? extends Annotation>> parserSet;

    private final static Map<Class<? extends Annotation>, InjectAnnotationParser<? extends Annotation>> PARSER_MAP = new HashMap<>();

    private final static Set<Class<? extends Annotation>> INJECT_ANNOTATIONS = new HashSet<>();

    public InjectAnnotationParserHolderImpl(Set<InjectAnnotationParser<?>> parserSet) {
        this.parserSet = parserSet;
        init();
    }

    private void init() {
        this.parserSet.forEach(this::register);
    }

    @Override
    public <T extends Annotation> InjectAnnotationParser<T> getParse(Class<T> annotationClass) {
        return (InjectAnnotationParser<T>) PARSER_MAP.get(annotationClass);
    }

    @Override
    public void register(InjectAnnotationParser<?> parser) {
        Class<? extends Annotation> superClassGenericType =
                (Class<? extends Annotation>) ClassUtils.getGenericInterfaceType(parser.getClass());
        PARSER_MAP.put(superClassGenericType, parser);
        INJECT_ANNOTATIONS.add(superClassGenericType);
    }

    @Override
    public <A extends Annotation> Object parse(A annotation) {
        InjectAnnotationParser<A> injectAnnotationParser = (InjectAnnotationParser<A>) PARSER_MAP.get(ClassUtils.getRealClass(annotation.getClass()));
        if (Objects.nonNull(injectAnnotationParser)) {
            return injectAnnotationParser.parse(annotation);
        }

        return null;
    }

    @Override
    public void inject(Object target) {
        Class<?> targetClass = target.getClass();
        try {
            for (PropertyDescriptor pd : BeanUtils.getPropertyDescriptors(targetClass)) {
                if ("class".equals(pd.getName())) {
                    continue;
                }
                Field field = ReflectionUtils.findField(targetClass, pd.getName());
                ReflectionUtils.makeAccessible(field);
                Method writeMethod = pd.getWriteMethod();
                ReflectionUtils.makeAccessible(writeMethod);

                List<Annotation> fieldAnnotations = Arrays.asList(field.getDeclaredAnnotations());
                fieldAnnotations.addAll(Arrays.asList(writeMethod.getDeclaredAnnotations()));

                if (!CollectionUtils.isEmpty(fieldAnnotations)) {
                    fieldAnnotations.parallelStream()
                            .filter(annotation -> INJECT_ANNOTATIONS.contains(ClassUtils.getRealClass(annotation.getClass())))
                            .forEach(LambdaUtils.consumerWrapper(annotation -> writeMethod.invoke(target, parse(annotation))));
                }

            }
        } catch (Exception ignore) {

        }

    }

}
