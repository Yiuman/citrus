package com.github.yiuman.citrus.support.cache;

import java.util.Collection;
import java.util.Map;

/**
 * 缓存
 *
 * @author yiuman
 * @date 2020/4/6
 */
public interface Cache<K, V> {

    void save(K key, V value);

    V find(K key);

    void update(K key, V value);

    void remove(K key);

    boolean contains(K key);

    Collection<K> keys();

    Map<K, V> map();

}