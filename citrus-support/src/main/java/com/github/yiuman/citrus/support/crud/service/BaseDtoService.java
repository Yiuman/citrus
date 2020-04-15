package com.github.yiuman.citrus.support.crud.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yiuman.citrus.support.utils.ConvertUtils;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * @author yiuman
 * @date 2020/4/15
 */
public abstract class BaseDtoService<E, K extends Serializable, D> implements CrudService<D, K> {

    private Class<D> dtoClass = currentDtoClass();

    private final BaseService<E, K> ekBaseService = new BaseService<E, K>() {
        @Override
        protected BaseMapper<E> getMapper() {
            return getBaseMapper();
        }

        @SuppressWarnings("unchecked")
        @Override
        public Class<E> getEntityType() {
            return (Class<E>) ReflectionKit.getSuperClassGenericType(BaseDtoService.this.getClass(), 0);
        }

        @Override
        public Class<K> getKeyType() {
            return BaseDtoService.this.getKeyType();
        }
    };

    protected abstract BaseMapper<E> getBaseMapper();

    @SuppressWarnings("unchecked")
    private Class<D> currentDtoClass() {
        return (Class<D>) ReflectionKit.getSuperClassGenericType(getClass(), 2);
    }

    protected Function<D, E> dtoToEntity() {
        return LambdaUtils.functionWrapper(d -> {
            E e = ekBaseService.getEntityType().newInstance();
            BeanUtils.copyProperties(d, e);
            return e;
        });
    }

    protected Function<E, D> entityToDto() {
        return LambdaUtils.functionWrapper(e -> {
            D d = dtoClass.newInstance();
            BeanUtils.copyProperties(e, d);
            return d;
        });
    }

    @Override
    public K save(D entity) throws Exception {
        if (!beforeSave(entity)) {
            return null;
        }
        K key = ekBaseService.save(dtoToEntity().apply(entity));
        afterSave(entity);
        return key;
    }

    @Override
    public boolean batchSave(Iterable<D> entityIterable) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    @Override
    public K update(D entity) throws Exception {
        if (!this.beforeUpdate(entity)) {
            return null;
        }
        K key = this.save(entity);
        afterUpdate(entity);
        return key;
    }


    @Override
    public boolean remove(D entity) throws Exception {
        if (!this.beforeRemove(entity)) {
            return false;
        }
        return ekBaseService.remove(dtoToEntity().apply(entity));
    }

    @Override
    public void batchRemove(Iterable<K> keys) {
        ekBaseService.batchRemove(keys);
    }

    @Override
    public void clear() {
        ekBaseService.clear();
    }

    @Override
    public D get(K key) {
        return entityToDto().apply(ekBaseService.get(key));
    }

    @Override
    public List<D> list() {
        return ConvertUtils.listConvert(dtoClass, ekBaseService.list());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<D> list(Wrapper<D> wrapper) {
        return ConvertUtils.listConvert(dtoClass, ekBaseService.list((Wrapper<E>) wrapper));
    }

    @Override
    public <P extends IPage<D>> P page(P page, Wrapper<D> queryWrapper) {
        //拷贝
        Page<E> entityPage = new Page<>();
        BeanUtils.copyProperties(page, entityPage);
        ekBaseService.page(entityPage, (QueryWrapper<E>) queryWrapper);
        //反拷贝
        BeanUtils.copyProperties(entityPage, page);
        return page;
    }
}
