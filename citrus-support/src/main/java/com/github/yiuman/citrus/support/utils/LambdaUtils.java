package com.github.yiuman.citrus.support.utils;

import com.github.yiuman.citrus.support.crud.query.Fn;
import com.github.yiuman.citrus.support.wrapper.*;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.*;

/**
 * Lambda工具
 *
 * @author yiuman
 * @date 2020/4/3
 */
public final class LambdaUtils {

    private LambdaUtils() {
    }

    public static <T> SerializedLambda serialized(Fn<T, ?> fn) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method writeReplace = fn.getClass().getDeclaredMethod("writeReplace");
        writeReplace.setAccessible(Boolean.TRUE);
        return (SerializedLambda) writeReplace.invoke(fn);
    }

    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public static <T> String getPropertyName(Fn<T, ?> fn) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        SerializedLambda serialized = serialized(fn);
        String implMethodName = serialized.getImplMethodName();
        if (implMethodName.startsWith("is")) {
            return implMethodName.substring(2);
        } else if (implMethodName.startsWith("get") || implMethodName.startsWith("set")) {
            return implMethodName.substring(3);
        }
        return null;
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
    public static <T, U> BiConsumer<T, U> biConsumerWrapper(BiConsumerWrapper<T, U, Exception> consumer) {
        return (t, u) -> {
            try {
                consumer.accept(t, u);
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

    public static <T> Predicate<T> predicateWrapper(PredicateWrapper<T, Exception> predicateWrapper) {
        return t -> {
            try {
                return predicateWrapper.test(t);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    public static <T> Supplier<T> supplierWrapper(SupplierWrapper<T, Exception>
                                                          supplierWrapper) {
        return () -> {
            try {
                return supplierWrapper.get();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
