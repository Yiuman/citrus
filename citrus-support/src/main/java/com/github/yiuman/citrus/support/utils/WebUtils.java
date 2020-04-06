package com.github.yiuman.citrus.support.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

/**
 * Web相关操作工具
 *
 * @author yiuman
 * @date 2020/4/4
 */
public final class WebUtils {

    private WebUtils() {
    }

    public static ServletRequestAttributes getServletRequestAttributes() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
    }

    public static HttpServletRequest getRequest() {
        return getServletRequestAttributes().getRequest();
    }

    public static String getRequestParam(String name) {
        return getRequest().getParameter(name);
    }

    public static HttpServletResponse getResponse() {
        return getServletRequestAttributes().getResponse();
    }

    public static Object getAttribute(String name, int scope) {
        return getServletRequestAttributes().getAttribute(name, scope);
    }

    public static <T> T requestDataBind(Class<T> objectClass, HttpServletRequest request) {
        //构造实体
        final T object = BeanUtils.instantiateClass(objectClass);
        WebRequestDataBinder dataBinder = new WebRequestDataBinder(object);
        dataBinder.bind(new ServletWebRequest(request));
        return object;
    }

    public static <T> void requestDataBind(final T object, HttpServletRequest request) throws Exception {
        //构造实体
        WebRequestDataBinder dataBinder = new WebRequestDataBinder(object);
        dataBinder.bind(new ServletWebRequest(request));
    }

    public static String getCookie(HttpServletRequest request,String name){
        Optional<Cookie> optionalCookie = Arrays.stream(request.getCookies()).filter(cookie -> name.equals(cookie.getName())).findFirst();
        return optionalCookie.map(Cookie::getValue).orElse(null);
    }
}
