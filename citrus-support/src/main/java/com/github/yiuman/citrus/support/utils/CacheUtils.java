package com.github.yiuman.citrus.support.utils;

import com.github.yiuman.citrus.support.cache.Cache;
import com.github.yiuman.citrus.support.cache.InMemoryCache;
import com.github.yiuman.citrus.support.cache.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;

/**
 * 缓存工具类
 *
 * @author yiuman
 * @date 2020/6/16
 */
public final class CacheUtils {

    public CacheUtils() {
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Cache<K, V> systemDefaultCache() {
        return Optional.ofNullable((Cache<K, V>) SpringUtils.getBean(RedisCache.class))
                .orElse(InMemoryCache.get());
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Cache<K, V> systemCache(String namespace) {
        return Optional.ofNullable((Cache<K, V>) SpringUtils.getBean(RedisCache.class, namespace))
                .orElse(InMemoryCache.get(namespace));
    }

    public static <K, V> InMemoryCache<K, V> newInMemoryCache(String namespace) {
        return InMemoryCache.get(namespace);
    }

    public static <K, V> InMemoryCache<K, V> defaultInMemoryCache() {
        return InMemoryCache.get(InMemoryCache.DEFAULT_NAMESPACE);
    }

    public static <K, V> RedisCache<K, V> newRedisCache(RedisTemplate<K, V> redisTemplate) {
        return new RedisCache<>(redisTemplate);
    }
}
