package com.github.yiuman.citrus.mda.converter;

import cn.hutool.core.util.ClassUtil;
import com.github.yiuman.citrus.support.utils.ConvertUtils;
import com.github.yiuman.citrus.support.utils.LambdaUtils;

/**
 * 元数据转换器
 *
 * @param <S> 源对象
 * @param <T> 目标对象
 * @author yiuman
 * @date 2021/4/29
 */
public interface Converter<S, T> {

    /**
     * 获取来源的类型
     *
     * @return 来源实体类型
     */
    @SuppressWarnings("unchecked")
    default Class<S> getSourceClass() {
        return (Class<S>) ClassUtil.getTypeArgument(getClass());
    }

    /**
     * 获取目标对象类型
     *
     * @return 目标对象类型
     */
    @SuppressWarnings("unchecked")
    default Class<T> getTargetClass() {
        return (Class<T>) ClassUtil.getTypeArgument(getClass(), 1);
    }

    /**
     * 直接复制的转化
     *
     * @param source 源数据
     * @return 目标数据
     */
    default T convert(S source) {
        return LambdaUtils
                .functionWrapper(item -> ConvertUtils.convert(getTargetClass(), source))
                .apply(source);
    }

}