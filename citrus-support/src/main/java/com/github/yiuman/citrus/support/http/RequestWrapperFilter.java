package com.github.yiuman.citrus.support.http;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author yiuman
 * @date 2022/6/13
 */
@Component
public class RequestWrapperFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String contentType = request.getContentType();
        if (StrUtil.isNotBlank(contentType)
                && (contentType.contains("multipart/form-data") || contentType.contains("application/x-www-form-urlencoded"))) {
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(new RequestWrapper(request), response);
        }

    }

    public static class RequestWrapper extends HttpServletRequestWrapper {

        private final byte[] body;
        private final ServletInputStream inputStream;
        private BufferedReader reader;

        public RequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            body = IoUtil.readBytes(request.getInputStream());
            inputStream = new RequestCachingInputStream(body);
        }

        public byte[] getBody() {
            return body;
        }

        @Override
        public BufferedReader getReader() throws UnsupportedEncodingException {
            if (reader == null) {
                reader = new BufferedReader(new InputStreamReader(inputStream, getCharacterEncoding()));
            }
            return reader;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            if (inputStream != null) {
                return inputStream;
            }
            return super.getInputStream();
        }
    }

    //代理一下ServletInputStream 里面真是内容为当前缓存的bytes
    private static class RequestCachingInputStream extends ServletInputStream {

        private final ByteArrayInputStream inputStream;

        RequestCachingInputStream(byte[] bytes) {
            inputStream = new ByteArrayInputStream(bytes);
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }

        @Override
        public boolean isFinished() {
            return inputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readlistener) {
        }

    }
}
