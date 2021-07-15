package com.github.yiuman.citrus.system.hook;

import com.github.yiuman.citrus.system.entity.Resource;
import com.github.yiuman.citrus.system.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 访问埋点器
 *
 * @author yiuman
 * @date 2021/7/15
 */
public interface AccessPointer {

    /**
     * 做埋点操作
     *
     * @param request  当前请求
     * @param user     用户
     * @param resource 访问的资源
     */
    void doPoint(HttpServletRequest request, User user, Resource resource);
}