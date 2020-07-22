package com.github.yiuman.citrus.support.crud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * @author yiuman
 * @date 2020/4/11
 */
public interface CrudMapper<T> extends BaseMapper<T> {

    /**
     * 保存实体
     *
     * @param entity 实体对象
     * @return 保存成功返回true，否则false
     * @throws Exception 数据库操作异常
     */
    default boolean saveEntity(T entity) throws Exception {
        if (null != entity) {
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
            //没找到主键的话直接插入
            String keyProperty = tableInfo.getKeyProperty();
            if (StringUtils.isBlank(keyProperty)) {
                return SqlHelper.retBool(insert(entity));
            }
            Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
            return StringUtils.checkValNull(idVal) || Objects.isNull(selectById((Serializable) idVal))
                    ? SqlHelper.retBool(insert(entity))
                    : SqlHelper.retBool(updateById(entity));
        }
        return false;
    }

    /**
     * 批量保存实体
     *
     * @param entityList 实体集合
     * @return 保存成功返回true，否则false
     * @throws Exception 数据库操作异常
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean saveBatch(Collection<T> entityList) throws Exception {
        entityList.forEach(LambdaUtils.consumerWrapper(this::saveEntity));
        return true;
    }


}