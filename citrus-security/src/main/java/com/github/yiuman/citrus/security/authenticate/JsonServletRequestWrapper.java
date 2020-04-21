package com.github.yiuman.citrus.security.authenticate;

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

    private Map<String, String[]> parameterMap;

    private byte[] bytes;

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
        this.bytes = body.getBytes();
        OBJECT_MAPPER.readValue(body, new TypeReference<Map<String, Object>>() {
        }).forEach((key, value) -> getParameterMap().put(key, new String[]{String.valueOf(value)}));
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
        String[] results = parameterMap.get(name);
        return (results == null || results.length <= 0) ? null : results[0];
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] results = parameterMap.get(name);
        if (results == null || results.length <= 0)
            return null;
        else
            return results;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        return new ServletInputStream() {
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
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }
}
