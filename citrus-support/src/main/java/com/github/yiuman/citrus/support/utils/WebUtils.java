package com.github.yiuman.citrus.support.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.ReadListener;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Web相关操作工具
 *
 * @author yiuman
 * @date 2020/4/4
 */
public final class WebUtils {

    private static final String APPLICATION_VND_MS_EXCEL = "application/vnd.ms-excel";

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

    public static String getCookie(HttpServletRequest request, String name) {
        Optional<Cookie> optionalCookie = Arrays.stream(request.getCookies()).filter(cookie -> name.equals(cookie.getName())).findFirst();
        return optionalCookie.map(Cookie::getValue).orElse(null);
    }

    public static <T> void exportExcel(HttpServletResponse response, Class<T> clazz, List<T> data, String name) throws IOException {
        response.setContentType(APPLICATION_VND_MS_EXCEL);
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "utf-8") + ".xls");
        EasyExcel.write(response.getOutputStream(), clazz).sheet("表").doWrite(data);
    }

    public static <T> void importExcel(MultipartFile file, Class<T> clazz, ReadListener<T> readListener) throws IOException {
        EasyExcel.read(file.getInputStream(),clazz,readListener);
    }
}
