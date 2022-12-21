package com.github.yiuman.citrus.security.authenticate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.util.ParameterMap;
import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * 适配JSON请求，兼容form-data与JSON
 *
 * @author yiuman
 * @date 2020/4/19
 */
public class JsonServletRequestWrapper extends HttpServletRequestWrapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private Map<String, String[]> parameterMap;
    private byte[] bytes;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public JsonServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        parseRequest(request);
    }

    private void parseRequest(HttpServletRequest request) throws IOException {
        String body = IOUtils.toString(request.getInputStream(), Charset.defaultCharset());
        if (StrUtil.isNotBlank(body)) {
            this.bytes = body.getBytes();
            OBJECT_MAPPER.readValue(body, new TypeReference<Map<String, Object>>() {
            }).forEach((key, value) -> getParameterMap().put(key, new String[]{String.valueOf(value)}));
        }
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        if (parameterMap == null) {
            parameterMap = new ParameterMap<>();
            parameterMap.putAll(super.getParameterMap());
        }
        return parameterMap;
    }

    @Override
    public String getParameter(String name) {
        if (CollUtil.isEmpty(parameterMap)) {
            return null;
        }

        String[] results = parameterMap.get(name);
        return ArrayUtil.isEmpty(results) ? null : results[0];
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] results = parameterMap.get(name);
        if (results == null || results.length <= 0) {
            return null;
        } else {
            return results;
        }
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        return new ServletInputStreamImpl(bytes);
    }

    static class ServletInputStreamImpl extends ServletInputStream {

        private final ByteArrayInputStream byteArrayInputStream;

        ServletInputStreamImpl(byte[] bytes) {
            this.byteArrayInputStream = new ByteArrayInputStream(bytes);
        }

        @Override
        public boolean isFinished() {
            return byteArrayInputStream.available() <= 0;
        }

        @Override
        public boolean isReady() {
            return byteArrayInputStream.available() > 0;
        }

        @Override
        public void setReadListener(ReadListener listener) {
        }

        @Override
        public int read() {
            return byteArrayInputStream.read();
        }
    }
}
