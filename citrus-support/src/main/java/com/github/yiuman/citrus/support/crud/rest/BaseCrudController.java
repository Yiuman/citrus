package com.github.yiuman.citrus.support.crud.rest;

import cn.hutool.core.thread.threadlocal.NamedThreadLocal;
import cn.hutool.core.util.ObjectUtil;
import com.github.yiuman.citrus.support.crud.groups.Modify;
import com.github.yiuman.citrus.support.crud.groups.Save;
import com.github.yiuman.citrus.support.crud.view.DataView;
import com.github.yiuman.citrus.support.crud.view.FormViewable;
import com.github.yiuman.citrus.support.crud.view.PageViewable;
import com.github.yiuman.citrus.support.crud.view.ViewHelper;
import com.github.yiuman.citrus.support.exception.ValidateException;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.utils.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 基础的RestfulCrud控制器
 *
 * @param <T> 实体类型
 * @param <K> 主键类型
 * @author yiuman
 * @date 2020/4/4
 */
@Slf4j
public abstract class BaseCrudController<T, K extends Serializable> extends BaseCrudRestful<T, K> implements PageViewable, FormViewable {

    /**
     * 视图数据
     */
    protected static final ThreadLocal<Object> VIEW_DATA = new NamedThreadLocal<>("view-data");

    @Override
    public Object createPageView() {
        return ViewHelper.createPageView(this, getPageViewData());
    }

    @Override
    public Object createFormView() {
        return ViewHelper.createFormView(this, getFormViewData());
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
    public ResponseEntity<?> getPageView(
            @RequestParam(defaultValue = PageAction.PAGE) String action,
            HttpServletRequest request) throws Exception {
        try {
            Object view;
            if (PageAction.PAGE.equals(action)) {
                VIEW_DATA.set(page(request));
                view = createPageView();
            } else {
                K key = (K) request.getParameter("key");
                if (ObjectUtil.isNotEmpty(key)) {
                    VIEW_DATA.set(get(key));
                }

                view = createFormView();
            }

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
        return ResponseEntity.ok(getProxy().page(request));
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<K> saveEntityForm(T entity) throws Exception {
        validateEntity(entity);
        return ResponseEntity.ok(getProxy().save(entity));
    }

    private void validateEntity(T entity) {
        K key = getService().getKey(entity);
        ValidateUtils.defaultValidateEntity(entity, Objects.isNull(key)
                ? new Class<?>[]{Save.class, Default.class}
                : new Class<?>[]{Modify.class, Default.class});
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<K> saveEntityBody(@RequestBody T entity) throws Exception {
        validateEntity(entity);
        return ResponseEntity.ok(getProxy().save(entity));
    }

    @DeleteMapping(Operations.DELETE)
    public ResponseEntity<Boolean> deleteByKey(@PathVariable @NotNull K key) throws Exception {
        return ResponseEntity.ok(getProxy().delete(key));
    }

    @GetMapping(Operations.GET)
    public ResponseEntity<T> getByKey(@PathVariable K key) {
        return ResponseEntity.ok(getProxy().get(key));
    }

    @PostMapping(value = Operations.BATCH_DELETE, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<Void> deleteByKeysForm(List<K> keys) throws Exception {
        if (CollectionUtils.isEmpty(keys)) {
            throw new ValidateException("keys con not be empty");
        }
        getProxy().batchDelete(keys);
        return ResponseEntity.ok();
    }

    @PostMapping(value = Operations.BATCH_DELETE, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Void> deleteByKeysBody(@RequestBody @NotNull List<K> keys) throws Exception {
        getProxy().batchDelete(keys);
        return ResponseEntity.ok();
    }

    /**
     * 导出
     */
    @GetMapping(value = Operations.EXPORT)
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        getProxy().exp(request, response);
    }

    /**
     * 导入
     */
    @PostMapping(value = Operations.IMPORT)
    public void importFile(MultipartFile file) throws Exception {
        getProxy().imp(file);
    }

    @SuppressWarnings("unchecked")
    protected <CRUD extends BaseCrudController<T, K>> CRUD getProxy() {
        Object proxy = AopContext.currentProxy();
        return Objects.nonNull(proxy) ? (CRUD) proxy : (CRUD) this;
    }

}
