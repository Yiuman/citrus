package com.github.yiuman.citrus.support.http;

/**
 * 回应状态码
 *
 * @author yiuman
 * @date 2020/2/10
 */
public interface ResponseStatusCode {

    /**
     * 成功
     */
    int OK = 0;

    /**
     * 请求参数有误
     */
    int BAD_REQUEST = 100400;

    /**
     * 认证失败
     */
    int UN_AUTHENTICATION = 100401;

    /**
     * 没权限
     */
    int NO_PERMISSIONS = 100403;

    /**
     * 没有对应的接口信息
     */
    int NOT_FOUND = 100404;

    /**
     * 服务器异常
     */
    int SERVER_ERROR = 100500;

    /**
     * 服务器作为网关或代理，从上游服务器收到无效响应
     */
    int BAD_INTERFACE = 100502;
}