package com.github.yiuman.citrus.support.crud.rest;

import com.github.yiuman.citrus.support.crud.CrudReadDataListener;
import com.github.yiuman.citrus.support.model.DialogView;
import com.github.yiuman.citrus.support.model.EditField;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础的RESTFUL
 *
 * @author yiuman
 * @date 2020/5/11
 */
@Slf4j
public abstract class BaseCrudRestful<T, K extends Serializable> extends BaseQueryRestful<T, K> implements CrudRestful<T, K> {

    /**
     * 创建列表分页页面
     *
     * @return 分页页面对象
     * @throws Exception 反射、数据库操作等异常
     */
    @Override
    protected Page<T> createPage() throws Exception {
        Page<T> page = super.createPage();
        page.setDialogView(createDialogView());
        return page;
    }

    /**
     * 创建数据编辑的对话框
     *
     * @return 对话框视图
     * @throws Exception 反射等操作异常
     */
    protected DialogView createDialogView() throws Exception {
        List<EditField> editFields = new ArrayList<>();
        ReflectionUtils.doWithFields(modelClass, field -> editFields.add(new EditField(field.getName(), field.getName())));
        return new DialogView(editFields);
    }

    @Override
    public K save(T entity) throws Exception {
        return getService().save(entity);
    }

    @Override
    public Boolean delete(K key) throws Exception {
        return getService().remove(getService().get(key));
    }


    @Override
    public void batchDelete(List<K> keys) throws Exception {
        getService().batchRemove(keys);
    }

    @Override
    public void imp(MultipartFile file) throws Exception {
        //CrudReadDataListener不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        WebUtils.importExcel(file, modelClass, new CrudReadDataListener<>(getService()));
    }


}
