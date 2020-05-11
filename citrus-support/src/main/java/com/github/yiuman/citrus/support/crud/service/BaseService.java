package com.github.yiuman.citrus.support.crud.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 基础的Service
 *
 * @author yiuman
 * @date 2020/4/15
 */
public abstract class BaseService<E, K extends Serializable> implements CrudService<E, K> {

    protected abstract BaseMapper<E> getMapper();

    @Transactional
    @Override
    public K save(E entity) throws Exception {
        if (!this.beforeSave(entity)) {
            return null;
        }
        if (null != entity) {
            Class<?> cls = getEntityType();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
            String keyProperty = tableInfo.getKeyProperty();
            Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
            K keyValue = getKey(entity);
            if (StringUtils.checkValNull(keyValue) || Objects.isNull(get(keyValue))) {
                getMapper().insert(entity);
                keyValue = getKey(entity);
            } else {
                getMapper().updateById(entity);
            }
            this.afterSave(entity);
            return keyValue;
        }

        return null;
    }


    @Override
    public boolean batchSave(Iterable<E> entityIterable) {
        try {
            entityIterable.forEach(LambdaUtils.consumerWrapper(this::save));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public K update(E entity) throws Exception {
        if (!this.beforeUpdate(entity)) {
            return null;
        }
        this.beforeUpdate(entity);
        K key = this.save(entity);
        this.afterUpdate(entity);
        return key;
    }

    @Override
    public boolean remove(E entity) throws Exception {
        return this.beforeRemove(entity) && getMapper().deleteById(getKey(entity)) > 1;
    }

    @Override
    public void batchRemove(Iterable<K> keys) {
        getMapper().deleteBatchIds((Collection<? extends Serializable>) keys);
    }

    @Override
    public void clear() {
        getMapper().deleteBatchIds(
                list().parallelStream()
                        .map(LambdaUtils.functionWrapper(this::getKey))
                        .collect(Collectors.toList()));
    }

    @Override
    public E get(K key) {
        return getMapper().selectById(key);
    }

    @Override
    public List<E> list() {
        return getMapper().selectList(Wrappers.emptyWrapper());
    }

    @Override
    public List<E> list(Wrapper<E> wrapper) {
        return getMapper().selectList(wrapper);
    }

    @Override
    public <P extends IPage<E>> P page(P page, Wrapper<E> queryWrapper) {
        return getMapper().selectPage(page, queryWrapper);
    }

}
