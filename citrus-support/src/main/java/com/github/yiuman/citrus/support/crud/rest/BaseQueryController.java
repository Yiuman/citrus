package com.github.yiuman.citrus.support.crud.rest;

import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.support.model.Page;
import org.springframework.aop.framework.AopContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Optional;

/**
 * 基本查询控制器
 *
 * @author yiuman
 * @date 2020/10/1
 */
public abstract class BaseQueryController<T, K extends Serializable> extends BaseQueryRestful<T, K> {

    @SuppressWarnings("unchecked")
    @GetMapping
    public ResponseEntity<Page<T>> getPageList(HttpServletRequest request) throws Exception {
        BaseQueryController<T,K> currentProxy = Optional.ofNullable((BaseQueryController<T, K>) AopContext.currentProxy()).orElse(this);
        return ResponseEntity.ok(currentProxy.page(request));
    }

    @GetMapping(Operations.GET)
    public ResponseEntity<T> getByKey(@PathVariable K key) {
        return ResponseEntity.ok(get(key));
    }

    /**
     * 导出
     */
    @GetMapping(value = Operations.EXPORT)
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        exp(request, response);
    }
}
