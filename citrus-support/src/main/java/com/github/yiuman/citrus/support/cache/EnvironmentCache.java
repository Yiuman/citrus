package com.github.yiuman.citrus.support.cache;

import com.github.yiuman.citrus.support.utils.CacheUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 根据环境配置进行切换的缓存，
 * 若使用了redis则用redis缓存
 * 没有则使用内存缓存
 *
 * @author yiuman
 * @date 2020/6/16
 */
public abstract class EnvironmentCache<K, V> implements Cache<K, V> {

    private final Cache<K, V> cache;

    public EnvironmentCache(String namespace) {
        this.cache = CacheUtils.systemCache(namespace);
    }

    @Override
    public void save(K key, V value) {
        cache.save(key, value);
    }

    @Override
    public V find(K key) {
        return cache.find(key);
    }

    @Override
    public void update(K key, V value) {
        cache.update(key, value);
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
    }

    @Override
    public boolean contains(K key) {
        return cache.contains(key);
    }

    @Override
    public Collection<K> keys() {
        return cache.keys();
    }

    @Override
    public Map<K, V> map() {
        return cache.map();
    }
}
