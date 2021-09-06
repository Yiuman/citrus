package com.github.yiuman.citrus.support.datasource;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.core.NamedThreadLocal;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 数据源切换持有者
 * 用于动态切换数据源以及多数据源事务管理
 *
 * @author yiuman
 * @date 2020/12/1
 */
public final class DynamicDataSourceHolder {

    /**
     * 后进先出
     */
    private static final ThreadLocal<Deque<String>> LOOKUP_KEY_HOLDER = new NamedThreadLocal<Deque<String>>("dynamic-datasource") {
        @Override
        protected Deque<String> initialValue() {
            return new ArrayDeque<>();
        }
    };

    private DynamicDataSourceHolder() {
    }

    /**
     * 获得当前线程数据源
     *
     * @return 数据源名称
     */
    public static String peek() {
        return LOOKUP_KEY_HOLDER.get().peek();
    }

    /**
     * 设置当前线程数据源
     * <p>
     * 如非必要不要手动调用，调用后确保最终清除
     * </p>
     *
     * @param ds 数据源名称
     */
    public static void push(String ds) {
        LOOKUP_KEY_HOLDER.get().push(ObjectUtil.isEmpty(ds) ? "" : ds);
    }

    /**
     * 清空当前线程数据源
     * <p>
     * 如果当前线程是连续切换数据源 只会移除掉当前线程的数据源名称
     * </p>
     */
    public static void poll() {
        Deque<String> deque = LOOKUP_KEY_HOLDER.get();
        deque.poll();
        if (deque.isEmpty()) {
            LOOKUP_KEY_HOLDER.remove();
        }
    }

    /**
     * 强制清空本地线程
     * <p>
     * 防止内存泄漏，如手动调用了push可调用此方法确保清除
     * </p>
     */
    public static void clear() {
        LOOKUP_KEY_HOLDER.remove();
    }
}
