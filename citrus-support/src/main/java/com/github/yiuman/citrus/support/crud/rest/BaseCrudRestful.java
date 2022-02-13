package com.github.yiuman.citrus.support.crud.rest;

import com.github.yiuman.citrus.support.crud.CrudReadDataListener;
import com.github.yiuman.citrus.support.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * 基础的RESTFUL
 *
 * @param <T> 实体类型
 * @param <K> 主键类型
 * @author yiuman
 * @date 2020/5/11
 */
@Slf4j
public abstract class BaseCrudRestful<T, K extends Serializable> extends BaseQueryRestful<T, K> implements CrudRestful<T, K> {

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
