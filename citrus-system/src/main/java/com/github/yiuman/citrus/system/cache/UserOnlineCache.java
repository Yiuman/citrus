package com.github.yiuman.citrus.system.cache;

import com.github.yiuman.citrus.support.cache.EnvironmentCache;
import com.github.yiuman.citrus.system.dto.UserOnlineInfo;
import org.springframework.stereotype.Component;

/**
 * 在线用户缓存
 *
 * @author yiuman
 * @date 2020/6/15
 */
@Component
public class UserOnlineCache extends EnvironmentCache<String, UserOnlineInfo> {

    private final static String USER_ONLINE_CACHE = "userOnlineCache";

    public UserOnlineCache() {
        super(USER_ONLINE_CACHE);
    }

}
