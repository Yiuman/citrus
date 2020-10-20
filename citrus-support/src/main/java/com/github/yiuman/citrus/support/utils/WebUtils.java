package com.github.yiuman.citrus.support.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.github.yiuman.citrus.support.http.JsonServletRequestWrapper;
import com.github.yiuman.citrus.support.model.Header;
import com.github.yiuman.citrus.support.model.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.*;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;

/**
 * Web相关操作工具
 *
 * @author yiuman
 * @date 2020/4/4
 */
@Slf4j
public final class WebUtils {

    private static final String APPLICATION_VND_MS_EXCEL = "application/vnd.ms-excel";

    /**
     * 忽略实体没有的字段
     */
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

    /**
     * 类型绑定及参数校验
     *
     * @param entityClass 当前绑定数据的类型
     * @param request     当前请求
     * @param <T>         绑定数据的类型
     * @return 已经绑定好的数据
     * @throws Exception 反射异常、参数校验异常
     */
    public static <T> T bindDataAndValidate(Class<T> entityClass, HttpServletRequest request) throws Exception {
        //构造认证模式实体
        final T supportEntity = WebUtils.requestDataBind(entityClass, request);
        //校验实体参数
        ValidateUtils.defaultValidateEntity(supportEntity);
        return supportEntity;

    }

    /**
     * 类型绑定请求数据
     *
     * @param objectClass 绑定的数据类型Class
     * @param request     当前请求
     * @param force       是否强制生成
     * @return 已经处理好的绑定数据
     * @throws Exception 反射异常
     */
    public static <T> T requestDataBind(Class<T> objectClass, HttpServletRequest request, boolean force) throws Exception {
        if (objectClass == null || request == null) {
            return null;
        }
        //若是get请求或者是form-data则先检验下有没参数
        boolean hasParameterRequest = request instanceof AbstractMultipartHttpServletRequest || request.getMethod().equals(HttpMethod.GET.name());
        if (!force && hasParameterRequest && CollectionUtils.isEmpty(request.getParameterMap())) {
            return null;
        }
        //构造实体
        T t = BeanUtils.instantiateClass(objectClass);
        requestDataBind(t, request);
        return t;
    }

    /**
     * 类型绑定请求数据
     *
     * @param objectClass 绑定的数据类型Class
     * @param request     当前请求
     * @param <T>         当前绑定实例Class的类型
     * @return 已经处理好的绑定数据
     * @throws Exception 反射异常
     */
    public static <T> T requestDataBind(Class<T> objectClass, HttpServletRequest request) throws Exception {
        return requestDataBind(objectClass, request, false);
    }

    /**
     * 实例绑定请求数据
     *
     * @param object  传入的绑定实例
     * @param request 当前请求
     * @param <T>     当前绑定实例类型
     * @throws Exception 反射异常
     */
    @SuppressWarnings("unchecked")
    public static <T> void requestDataBind(T object, HttpServletRequest request) throws Exception {
        if (request instanceof AbstractMultipartHttpServletRequest || request.getMethod().equals(HttpMethod.GET.name())) {
            WebRequestDataBinder dataBinder = new WebRequestDataBinder(object);
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

    /**
     * 获取cookie值
     *
     * @param request 当前请求
     * @param name    cookie参数名
     * @return cookie值
     */
    public static String getCookie(HttpServletRequest request, String name) {
        Optional<Cookie> optionalCookie = Arrays.stream(request.getCookies()).filter(cookie -> name.equals(cookie.getName())).findFirst();
        return optionalCookie.map(Cookie::getValue).orElse(null);
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * <p>
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     * <p>
     * 用户真实IP为： 192.168.1.110
     *
     * @param request 请求
     * @return IPADDRESS ip地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static void exportJson(HttpServletResponse response, Object data, String name) throws IOException {
        response.setContentType(APPLICATION_VND_MS_EXCEL);
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "utf-8") + ".json");
        response.getWriter().write(OBJECT_MAPPER.writeValueAsString(data));
    }


    /**
     * 导出excel文档
     *
     * @param response 当前响应
     * @param clazz    类型
     * @param data     数据
     * @param name     导出文件名
     * @param <T>      导出定义类的类型
     * @throws IOException IO异常
     */
    public static <T> void exportExcel(HttpServletResponse response, Class<T> clazz, List<T> data, String name) throws IOException {
        response.setContentType(APPLICATION_VND_MS_EXCEL);
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "utf-8") + ".xls");
        EasyExcel.write(response.getOutputStream(), clazz)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("sheet1").doWrite(data);
    }

    /**
     * 动态列导出Excel
     *
     * @param response 响应
     * @param headers  表头
     * @param data     数据
     * @param name     文件 名
     * @throws IOException IO异常
     */
    public static void exportExcel(HttpServletResponse response, List<List<String>> headers, List<List<Object>> data, String name) throws IOException {
        response.setContentType(APPLICATION_VND_MS_EXCEL);
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "utf-8") + ".xls");
        EasyExcel.write(response.getOutputStream())
                .head(headers)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("sheet1")
                .doWrite(data);
    }

    /**
     * 动态列导出Excel
     *
     * @param response 响应
     * @param page     分页对象
     * @param name     文件 名
     * @throws IOException IO异常
     */
    public static void exportExcel(HttpServletResponse response, Page<?> page, String name) throws IOException, NoSuchFieldException {
        response.setContentType(APPLICATION_VND_MS_EXCEL);
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "utf-8") + ".xls");

        List<Header> pageHeaders = page.getHeaders();
        List<?> records = page.getRecords();
        List<List<String>> headers = new ArrayList<>(pageHeaders.size());
        List<List<Object>> data = new ArrayList<>(records.size());
        Map<String, Map<String, Object>> recordExtend = page.getRecordExtend();

        pageHeaders.forEach(header -> headers.add(Collections.singletonList(header.getText())));
        //这里记录字段与表头的对应关系，方便后边操作，遍历一次后之后不需要重新取
        final Class<?> recordClass = records.get(0).getClass();
        Field recordKeyField =ReflectionUtils.findField(recordClass, page.getItemKey());
        recordKeyField.setAccessible(true);
        final Map<String, Field> fieldMap = new HashMap<>();
        records.forEach(record -> {
            List<Object> objects = new ArrayList<>(pageHeaders.size());
            pageHeaders.forEach(header -> {
                Object fieldValue;
                String fieldName = header.getValue();
                try {
                    Field field = Optional.ofNullable(fieldMap.get(fieldName)).orElse(recordClass.getDeclaredField(header.getValue()));
                    field.setAccessible(true);
                    fieldValue = field.get(record);
                    fieldMap.put(fieldName, field);

                } catch (NoSuchFieldException | IllegalAccessException e) {
                    if (CollectionUtils.isEmpty(recordExtend)) {
                        page.initFunctionalRecords();
                    }
                    try {
                        Map<String, Object> singleRecordExtendData = recordExtend.get(recordKeyField.get(record).toString());
                        fieldValue = singleRecordExtendData.get(fieldName);
                    } catch (IllegalAccessException ex) {
                        fieldValue = "not founded";
                    }

                }
                objects.add(fieldValue);
            });

            data.add(objects);
        });

        exportExcel(response, headers, data, name);
    }

    /**
     * 导入文件
     *
     * @param file         文件
     * @param clazz        定义的导入类型
     * @param readListener 读监听器
     * @param <T>          导入定义类型泛型
     * @throws IOException IO异常
     */
    public static <T> void importExcel(MultipartFile file, Class<T> clazz, ReadListener<T> readListener) throws IOException {
        EasyExcel.read(file.getInputStream(), clazz, readListener).doReadAll();
    }


    /**
     * 根据当前请求获取controller定义了的路径
     * 如：@RequestMapping('/user/{key}') 则返回/user/{key}，若没定义则返回null
     *
     * @param request 当前请求
     * @return 匹配的定义路径
     * @throws Exception 反射异常
     */
    public static String getRequestMapping(HttpServletRequest request) throws Exception {
        RequestMappingHandlerMapping requestMappingHandlerMapping = SpringUtils.getBean(RequestMappingHandlerMapping.class);
        HandlerExecutionChain handler = requestMappingHandlerMapping.getHandler(request);
        if (handler == null) {
            return null;
        }

        HandlerMethod handlerHandler = (HandlerMethod) handler.getHandler();
        String combinePath = requestMappingHandlerMapping
                .getPathMatcher()
                .combine(getAnnotatedElementMapping(handlerHandler.getBeanType()), getAnnotatedElementMapping(handlerHandler.getMethod()));
        if (StringUtils.hasLength(combinePath) && !combinePath.startsWith("/")) {
            combinePath = "/" + combinePath;
        }

        return combinePath;
    }


    /**
     * 获取当前请求的MVC定义了的路径
     *
     * @return controller定义了的路径
     * @throws Exception 反射异常
     */
    public static String getCurrentRequestMapping() throws Exception {
        return getRequestMapping(getRequest());
    }

    /**
     * 根据当前请求获取当前请求的mvc处理器方法
     *
     * @param request 当前请求
     * @return mvc处理器方法
     * @throws Exception 反射异常
     */
    public static HandlerMethod getRequestHandlerMethod(HttpServletRequest request) throws Exception {
        RequestMappingHandlerMapping requestMappingHandlerMapping = SpringUtils.getBean(RequestMappingHandlerMapping.class);
        HandlerExecutionChain handler = requestMappingHandlerMapping.getHandler(request);
        if (handler == null) {
            return null;
        }

        return (HandlerMethod) handler.getHandler();
    }


    /**
     * 根据当前请求获取请求处理的方法
     *
     * @param request 当前请求
     * @return Method，处理这个请求的真正的代码方法
     * @throws Exception 反射异常
     */
    public static Method getRequestRealHandleMethod(HttpServletRequest request) throws Exception {
        HandlerMethod requestHandlerMethod = getRequestHandlerMethod(request);
        return Objects.isNull(requestHandlerMethod) ? null : requestHandlerMethod.getMethod();
    }

    /**
     * 根据当前请求获取请求处理类
     *
     * @param request 当前请求
     * @return class，处理这个请求的类
     * @throws Exception 反射异常
     */
    public static Class<?> getRequestHandlerBeanType(HttpServletRequest request) throws Exception {
        HandlerMethod requestHandlerMethod = getRequestHandlerMethod(request);
        return Objects.isNull(requestHandlerMethod) ? null : requestHandlerMethod.getBeanType();
    }


    /**
     * 获取可注解节点的Mapping值
     *
     * @param element 注解节点，如Class、Method等
     * @return requestMappingPath
     */
    public static String getAnnotatedElementMapping(AnnotatedElement element) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
        if (requestMapping == null) {
            return null;
        }
        String[] paths = requestMapping.path();
        if (ObjectUtils.isEmpty(paths) || !StringUtils.hasLength(paths[0])) {
            return null;
        }
        return paths[0];
    }
}
