package com.github.yiuman.citrus.support.crud;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO实体CRUD抽象服务类
 *
 * @author yiuman
 * @date 2020/4/4
 */
@SuppressWarnings("unchecked")
public class BaseDtoCrudService<M extends BaseMapper<E>, E, D, K>
        extends BaseKeyService<M, E, K>
        implements CrudService<D, K> {

    protected final Class<D> dtoClass = currentDtoClass();

    public BaseDtoCrudService() {
    }

    private Class<D> currentDtoClass() {
        return (Class<D>) ReflectionKit.getSuperClassGenericType(getClass(), 2);
    }

    @Override
    public K saveEntity(D dto) throws Exception {
        E realEntity = (E) entityClass.newInstance();
        BeanUtils.copyProperties(dto, realEntity);
        save(realEntity);
        return key(realEntity);
    }

    @Override
    public void delete(K key) throws Exception {
        removeById((Serializable) key);
    }

    @Override
    public D get(K key) throws Exception {
        E entity = getById((Serializable) key);
        D dto = dtoClass.newInstance();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public List<D> getList() throws Exception {
        return list().parallelStream().map(LambdaUtils.functionWrapper(user -> {
            D dto = dtoClass.newInstance();
            BeanUtils.copyProperties(user, dto);
            return dto;
        })).collect(Collectors.toList());
    }

    @Override
    public <P extends IPage<D>> P selectPage(P page, Wrapper<D> queryWrapper) {
        Page<E> entityPage = new Page<>();
        BeanUtils.copyProperties(page, entityPage);
        QueryWrapper<E> wrapper = (QueryWrapper<E>) queryWrapper;
        getBaseMapper().selectPage(entityPage,wrapper );
        BeanUtils.copyProperties(entityPage, page);
        return page;
    }


}
