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

    /**
     * 保存
     *
     * @param key   键
     * @param value 值
     */
    void save(K key, V value);

    /**
     * 根据键找到对应的值
     *
     * @param key 键
     * @return 值
     */
    V find(K key);

    /**
     * 更新缓存
     *
     * @param key   键
     * @param value 值
     */
    void update(K key, V value);

    /**
     * 根据键清楚缓存
     *
     * @param key 键
     */
    void remove(K key);

    /**
     * 判断是否已经包含某个缓存内容
     *
     * @param key 键
     * @return true/false
     */
    boolean contains(K key);

    /**
     * 获取所有键的集合
     *
     * @return 键的集合
     */
    Collection<K> keys();

    /**
     * 将缓存内容转成Map对象
     *
     * @return 缓存内容的Map集合
     */
    Map<K, V> map();

}