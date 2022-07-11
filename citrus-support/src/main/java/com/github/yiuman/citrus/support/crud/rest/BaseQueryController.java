package com.github.yiuman.citrus.support.crud.rest;

import cn.hutool.core.thread.threadlocal.NamedThreadLocal;
import com.github.yiuman.citrus.support.crud.view.DataView;
import com.github.yiuman.citrus.support.crud.view.PageViewable;
import com.github.yiuman.citrus.support.crud.view.ViewHelper;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.utils.SpringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * 基本查询控制器
 *
 * @param <T> 实体类型
 * @param <K> 主键类型
 * @author yiuman
 * @date 2020/10/1
 */
public abstract class BaseQueryController<T, K extends Serializable> extends BaseQueryRestful<T, K> implements PageViewable {

    protected static final ThreadLocal<Object> VIEW_DATA = new NamedThreadLocal<>("view-data");

    @Override
    public Object createPageView() {
        return ViewHelper.createPageView(this, getPageViewData());
    }

    protected Object getViewData() {
        return VIEW_DATA.get();
    }

    @SuppressWarnings("unchecked")
    protected Page<T> getPageViewData() {
        return (Page<T>) getViewData();
    }

    @SuppressWarnings("unchecked")
    protected T getFormViewData() {
        return (T) getViewData();
    }

    @SuppressWarnings("unchecked")
    @GetMapping(Operations.VIEW)
    public ResponseEntity<?> getPageView(HttpServletRequest request) throws Exception {
        try {
            VIEW_DATA.set(page(request));
            Object view = createPageView();
            if (view instanceof DataView) {
                ((DataView<Object>) view).setData(getViewData());
            }
            return ResponseEntity.ok(view);

        } finally {
            VIEW_DATA.remove();
        }

    }

    @GetMapping
    public ResponseEntity<Page<T>> getPageList(HttpServletRequest request) throws Exception {
        BaseQueryController<T, K> proxy = SpringUtils.getProxy(this);
        return ResponseEntity.ok(proxy.page(request));
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
