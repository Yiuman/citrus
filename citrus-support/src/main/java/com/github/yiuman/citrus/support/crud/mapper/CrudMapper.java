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
import java.util.*;
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
     * @param entityList 实体集合
     * @return 保存成功返回true，否则false
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean saveBatch(Collection<T> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return false;
        }
        Optional<T> first = entityList.stream().findFirst();
        if (!first.isPresent()) {
            return false;
        }

        T entity = first.get();
        Class<?> realEntityClass = ClassUtils.getRealClass(entity.getClass());
        TableInfo tableInfo = TableInfoHelper.getTableInfo(realEntityClass);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        //根据是否存在主键进行分组，有主键为true,没主键为false
        Map<Boolean, List<T>> collect = entityList.stream().collect(Collectors.groupingBy((item) -> {
            Object keyFieldValue = ReflectionKit.getFieldValue(item, tableInfo.getKeyProperty());
            //已经存在与数据库
            return StringUtils.checkValNull(keyFieldValue)
                    || Objects.isNull(selectById((Serializable) keyFieldValue));
        }));

        Class<?> realThisClass = ClassUtils.getRealClass(this.getClass());
        boolean result = true;
        //insert分组
        List<T> inserts = collect.get(true);
        if (!CollectionUtils.isEmpty(inserts)) {
            String sqlStatement = SqlHelper.getSqlStatement(realThisClass, SqlMethod.INSERT_ONE);
            result &= executeBatch(inserts, (sqlSession, handlerEntity) -> sqlSession.insert(sqlStatement, handlerEntity));
        }
        //update分组
        List<T> updates = collect.get(false);
        if (CollectionUtils.isEmpty(updates)) {
            String sqlStatement = SqlHelper.getSqlStatement(realThisClass, SqlMethod.UPDATE_BY_ID);
            result &= executeBatch(updates, (sqlSession, handlerEntity) -> {
                MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
                param.put(Constants.ENTITY, handlerEntity);
                sqlSession.update(sqlStatement, param);
            });
        }

        return result;

//        entityList.forEach(LambdaUtils.consumerWrapper(this::saveEntity));
    }

    /**
     * 批量执行
     *
     * @param entities 当前实体的集合
     * @param consumer SqlSession消费者，用户处理单单个实体
     * @return 是否执行成功 true/false
     */
    default boolean executeBatch(Collection<T> entities, BiConsumer<SqlSession, T> consumer) {
        Class<?> realClass = ClassUtils.getRealClass(getClass());
        Class<?> entityClass = ReflectionKit.getSuperClassGenericType(realClass, 0);
        return SqlHelper.executeBatch(
                entityClass,
                LogFactory.getLog(ClassUtils.getRealClass(getClass())),
                entities,
                entities.size(),
                consumer);
    }


}