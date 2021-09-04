package com.github.yiuman.citrus.support.cache;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @param <K> 键值对键类型
 * @param <V> 键值对值类型
 * @author yiuman
 * @date 2020/4/6
 */
public class MapCache<K, V> implements Cache<K, V> {

    private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>(256);

    public MapCache() {
    }

    @Override
    public void save(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public V find(K key) {
        return cache.get(key);
    }

    @Override
    public void update(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
    }

    @Override
    public boolean contains(K key) {
        return cache.containsKey(key);
    }

    @Override
    public Collection<K> keys() {
        return cache.keySet();
    }

    @Override
    public Map<K, V> map() {
        return this.cache;
    }
}
