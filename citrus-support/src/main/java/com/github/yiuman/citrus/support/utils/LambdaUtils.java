package com.github.yiuman.citrus.support.utils;

import com.github.yiuman.citrus.support.wrapper.BiConsumerWrapper;
import com.github.yiuman.citrus.support.wrapper.ConsumerWrapper;
import com.github.yiuman.citrus.support.wrapper.FunctionWrapper;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Lambda工具
 *
 * @author yiuman
 * @date 2020/4/3
 */
public final class LambdaUtils {

    private LambdaUtils() {
    }

    /**
     * 用于处理lambda检查异常
     *
     * @param consumer 可抛异常consumer
     * @param <T>      类型
     * @return Consumer<T>
     */
    public static <T> Consumer<T> consumerWrapper(ConsumerWrapper<T, Exception> consumer) {
        return t -> {
            try {
                consumer.accept(t);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    /**
     * 用于处理lambda检查异常
     *
     * @param consumer 可抛异常consumer
     * @param <T>      类型
     * @return Consumer<T>
     */
    public static <T,U> BiConsumer<T,U> biConsumerWrapper(BiConsumerWrapper<T,U, Exception> consumer) {
        return (t,u) -> {
            try {
                consumer.accept(t,u);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    public static <T, R> Function<T, R> functionWrapper(FunctionWrapper<T, R, Exception>
                                                                functionWrapper) {
        return t -> {
            try {
                return functionWrapper.apply(t);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
