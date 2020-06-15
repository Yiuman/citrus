package com.github.yiuman.citrus.system.cache;

import com.github.yiuman.citrus.support.cache.Cache;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * @author yiuman
 * @date 2020/6/15
 */
@Component
public class UserOnlineCache implements Cache<String, User> {


    public UserOnlineCache() {
    }

    @Override
    public void save(String key, User value) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public User find(String key) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public void update(String key, User value) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public void remove(String key) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public boolean contains(String key) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public Collection<String> keys() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public Map<String, User> map() {
        throw new UnsupportedOperationException("Method not implemented.");
    }
}
