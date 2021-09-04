package com.github.yiuman.citrus.support.cache;

import com.github.yiuman.citrus.support.utils.CacheUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * 根据环境配置进行切换的缓存，
 * 若使用了redis则用redis缓存
 * 没有则使用内存缓存
 *
 * @author yiuman
 * @date 2020/6/16
 * @param <K> 键
 * @param <V> 值
 */
public abstract class AbstractEnvironmentCache<K, V> implements Cache<K, V> {

    private final String namespace;

    private Cache<K, V> cache;

    public AbstractEnvironmentCache(String namespace) {
        this.namespace = namespace;
    }

    private Cache<K, V> getCache() {
        return cache = Optional.ofNullable(cache).orElse(CacheUtils.systemCache(namespace));
    }

    @Override
    public void save(K key, V value) {
        getCache().save(key, value);
    }

    @Override
    public V find(K key) {
        return getCache().find(key);
    }

    @Override
    public void update(K key, V value) {
        getCache().update(key, value);
    }

    @Override
    public void remove(K key) {
        getCache().remove(key);
    }

    @Override
    public boolean contains(K key) {
        return getCache().contains(key);
    }

    @Override
    public Collection<K> keys() {
        return getCache().keys();
    }

    @Override
    public Map<K, V> map() {
        return getCache().map();
    }
}
