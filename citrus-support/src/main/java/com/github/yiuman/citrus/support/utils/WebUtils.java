package com.github.yiuman.citrus.support.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.ReadListener;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.github.yiuman.citrus.support.http.JsonServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest;

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
@Slf4j
public final class WebUtils {

    private static final String APPLICATION_VND_MS_EXCEL = "application/vnd.ms-excel";

    //忽略实体没有的字段
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);

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

    public static <T> T convertRequestModeEntity(Class<T> entityClass, HttpServletRequest request) throws Exception {
        //构造认证模式实体
        final T supportEntity = WebUtils.requestDataBind(entityClass, request);
        //校验实体参数
        ValidateUtils.defaultValidateEntity(supportEntity);
        return supportEntity;

    }

    public static <T> T requestDataBind(Class<T> objectClass, HttpServletRequest request) throws Exception {
        if (objectClass == null || request == null) {
            return null;
        }
        //构造实体
        T t = BeanUtils.instantiateClass(objectClass);
        requestDataBind(t, request);
        return t;
    }

    @SuppressWarnings("unchecked")
    public static <T> void requestDataBind(final T object, HttpServletRequest request) throws Exception {
        WebRequestDataBinder dataBinder = new WebRequestDataBinder(object);
        if (request instanceof AbstractMultipartHttpServletRequest || request.getMethod().equals(HttpMethod.GET.name())) {
            dataBinder.bind(new ServletWebRequest(request));
        } else {
            try {
                if (!(request instanceof JsonServletRequestWrapper)) {
                    request = new JsonServletRequestWrapper(request);
                }

                if (object.getClass().isArray()) {
                    BeanUtils.copyProperties(object, ((JsonServletRequestWrapper) request).getArray());
                } else {
                    T realT = (T) OBJECT_MAPPER.readValue(request.getInputStream(), object.getClass());
                    BeanUtils.copyProperties(realT, object);
                }

            } catch (MismatchedInputException ex) {
                log.info("请求数据转换异常", ex);
            }

        }

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
        EasyExcel.read(file.getInputStream(), clazz, readListener);
    }
}
