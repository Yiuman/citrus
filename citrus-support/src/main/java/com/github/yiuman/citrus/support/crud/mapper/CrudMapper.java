package com.github.yiuman.citrus.support.crud.mapper;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.github.yiuman.citrus.support.utils.ClassUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * 通用的CRUD-DAO
 *
 * @author yiuman
 * @date 2020/4/11
 */
public interface CrudMapper<T> extends BaseMapper<T> {

    /**
     * 保存实体
     *
     * @param entity 实体对象
     * @return 保存成功返回true，否则false
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean saveEntity(T entity) {
        if (null != entity) {
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
            //没找到主键的话直接插入
            String keyProperty = tableInfo.getKeyProperty();
            if (StringUtils.isBlank(keyProperty)) {
                return SqlHelper.retBool(insert(entity));
            }
            Object idVal = ReflectionKit.getFieldValue(entity, tableInfo.getKeyProperty());
            return StringUtils.checkValNull(idVal) || Objects.isNull(selectById((Serializable) idVal))
                    ? SqlHelper.retBool(insert(entity))
                    : SqlHelper.retBool(updateById(entity));
        }
        return false;
    }

    /**
     * 批量保存实体
     *
     * @param entities 实体集合
     * @return 保存成功返回true，否则false
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean saveBatch(Collection<T> entities) {
        if (Objects.isNull(entities) || entities.isEmpty()) {
            return true;
        }

        T entity = entities.stream().findAny().get();
        Class<?> realEntityClass = ClassUtils.getRealClass(entity.getClass());
        TableInfo tableInfo = TableInfoHelper.getTableInfo(realEntityClass);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        //根据是否存在主键进行分组，有主键为true,没主键为false
        Map<Boolean, List<T>> collect = entities.stream().collect(Collectors.groupingBy((item) -> {
            Object keyFieldValue = ReflectionKit.getFieldValue(item, tableInfo.getKeyProperty());
            //已经存在与数据库
            return StringUtils.checkValNull(keyFieldValue)
                    || Objects.isNull(selectById((Serializable) keyFieldValue));
        }));

        return insertBatch(collect.get(true)) && updateBatch(collect.get(false));

    }

    /**
     * 批量插入
     *
     * @param entities 实体集合
     * @return 集合为空或插入成功返回true 插入失败返回false
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean insertBatch(Collection<T> entities) {
        return CollectionUtils.isEmpty(entities)
                || executeBatch(entities,
                (sqlSession, handlerEntity) -> sqlSession.insert(
                        SqlHelper.getSqlStatement(ClassUtils.getRealClass(this.getClass()), SqlMethod.INSERT_ONE),
                        handlerEntity
                ));
    }

    /**
     * 批量更新
     *
     * @param entities 实体集合
     * @return 集合为空或更新成功返回true 更新失败返回false
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean updateBatch(Collection<T> entities) {
        return CollectionUtils.isEmpty(entities)
                || executeBatch(entities,
                (sqlSession, handlerEntity) -> {
                    MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
                    param.put(Constants.ENTITY, handlerEntity);
                    sqlSession.update(SqlHelper.getSqlStatement(ClassUtils.getRealClass(this.getClass()), SqlMethod.UPDATE_BY_ID), param);
                });
    }

    /**
     * 批量删除
     *
     * @param entities 实体集合
     * @return 若集合为空或删除成功返回true 否则返回false
     */
    @SuppressWarnings({"unused", "RedundantSuppression"})
    @Transactional(rollbackFor = Exception.class)
    default boolean deleteBatch(Collection<T> entities) {
        if (Objects.isNull(entities) || entities.isEmpty()) {
            return true;
        }
        Class<?> realEntityClass = ClassUtils.getRealClass(entities.stream().findAny().get().getClass());
        return deleteBatchIds(entities.stream().map(item ->
                (Serializable) ReflectionKit.getFieldValue(
                        item,
                        TableInfoHelper.getTableInfo(ClassUtils.getRealClass(realEntityClass)).getKeyProperty()))
                .filter(Objects::nonNull).collect(Collectors.toList()))
                > 0;
    }

    /**
     * 批量执行
     *
     * @param entities 当前实体的集合
     * @param consumer SqlSession消费者，用户处理单单个实体
     * @return 是否执行成功 true/false
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean executeBatch(Collection<T> entities, BiConsumer<SqlSession, T> consumer) {
        if (Objects.isNull(entities) || entities.isEmpty()) {
            return true;
        }

        Class<?> realEntityClass = ClassUtils.getRealClass(entities.stream().findAny().get().getClass());
        return SqlHelper.executeBatch(
                realEntityClass,
                LogFactory.getLog(ClassUtils.getRealClass(getClass())),
                entities,
                entities.size(),
                consumer);
    }


}