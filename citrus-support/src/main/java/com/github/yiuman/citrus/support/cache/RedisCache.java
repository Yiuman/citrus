package com.github.yiuman.citrus.support.cache;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

/**
 * redis缓存
 *
 * @author yiuman
 * @date 2020/6/16
 */
public class RedisCache<K, V> implements Cache<K, V> {

    private final RedisTemplate<K, V> redisTemplate;

    public RedisCache(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(K key, V value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public V find(K key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void update(K key, V value) {
        this.save(key, value);
    }

    @Override
    public void remove(K key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean contains(K key) {
        return redisTemplate.hasKey(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<K> keys() {
        Set<byte[]> keys = redisTemplate.getConnectionFactory().getConnection().keys("*".getBytes());
        if (CollectionUtils.isEmpty(keys)) {
            return null;
        }
        final Set<K> ketSet = new HashSet<>(keys.size());
        keys.forEach(keyByte -> ketSet.add((K) redisTemplate.getKeySerializer().deserialize(keyByte)));
        return ketSet;
    }

    @Override
    public Map<K, V> map() {
        Collection<K> keys = keys();
        final Map<K, V> map = new HashMap<>(keys.size());
        keys.forEach(key -> map.put(key, this.find(key)));
        return map;
    }
}
