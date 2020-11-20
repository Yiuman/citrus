package com.github.yiuman.citrus.support.utils;

import java.util.concurrent.*;

/**
 * 线程执行工具类
 *
 * @author yiuman
 * @date 2020/11/20
 */
public final class ThreadUtils {


    public static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;

    public static final int MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    public static final int KEEP_ALIVE_TIME = 1000;

    public static final int BLOCK_QUEUE_SIZE = 1000;

    public static void executor(Runnable runnable) {
        getThreadPoolExecutor().execute(runnable);
    }

    public static <T> Future<T> submit(Callable<T> callable) {
        return getThreadPoolExecutor().submit(callable);
    }

    /**
     * 获取线程池对象
     *
     * @return 线程池
     */
    public static ThreadPoolExecutor getThreadPoolExecutor() {
        return TreadPoolHolder.THREAD_POOL;
    }

    static class TreadPoolHolder {
        private static final ThreadPoolExecutor THREAD_POOL = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(BLOCK_QUEUE_SIZE), new ThreadPoolExecutor.CallerRunsPolicy());
    }

}
