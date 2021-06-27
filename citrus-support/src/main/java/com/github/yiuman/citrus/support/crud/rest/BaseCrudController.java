package com.github.yiuman.citrus.support.crud.rest;

import com.github.yiuman.citrus.support.exception.ValidateException;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.utils.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * 基础的RestfulCrud控制器
 *
 * @author yiuman
 * @date 2020/4/4
 */
@Slf4j
public abstract class BaseCrudController<T, K extends Serializable> extends BaseCrudRestful<T, K> {

    @SuppressWarnings("unchecked")
    @GetMapping
    public ResponseEntity<Page<T>> getPageList(HttpServletRequest request) throws Exception {
        BaseCrudController<T, K> currentProxy = Optional.ofNullable((BaseCrudController<T, K>) AopContext.currentProxy()).orElse(this);
        return ResponseEntity.ok(currentProxy.page(request));
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<K> saveEntityForm(T entity) throws Exception {
        ValidateUtils.defaultValidateEntity(entity);
        return ResponseEntity.ok(save(entity));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<K> saveEntityBody(@RequestBody @Validated T entity) throws Exception {
        return ResponseEntity.ok(save(entity));
    }

    @SuppressWarnings("MVCPathVariableInspection")
    @DeleteMapping(Operations.DELETE)
    public ResponseEntity<Boolean> deleteByKey(@PathVariable @NotNull K key) throws Exception {
        return ResponseEntity.ok(delete(key));
    }

    @SuppressWarnings("MVCPathVariableInspection")
    @GetMapping(Operations.GET)
    public ResponseEntity<T> getByKey(@PathVariable K key) {
        return ResponseEntity.ok(get(key));
    }

    @PostMapping(value = Operations.BATCH_DELETE, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<Void> deleteByKeysForm(List<K> keys) throws Exception {
        if (CollectionUtils.isEmpty(keys)) {
            throw new ValidateException("keys con not be empty");
        }
        super.batchDelete(keys);
        return ResponseEntity.ok();
    }

    @PostMapping(value = Operations.BATCH_DELETE, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Void> deleteByKeysBody(@RequestBody @NotNull List<K> keys) throws Exception {
        super.batchDelete(keys);
        return ResponseEntity.ok();
    }

    /**
     * 导出
     */
    @GetMapping(value = Operations.EXPORT)
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        exp(request, response);
    }

    /**
     * 导入
     */
    @PostMapping(value = Operations.IMPORT)
    public void importFile(MultipartFile file) throws Exception {
        imp(file);
    }

}
