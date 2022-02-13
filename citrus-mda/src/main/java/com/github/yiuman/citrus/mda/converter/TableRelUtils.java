package com.github.yiuman.citrus.mda.converter;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.mda.entity.TableRel;
import com.github.yiuman.citrus.mda.exception.ConverterException;
import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.support.utils.CrudUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author yiuman
 * @date 2021/4/29
 */
public final class TableRelUtils {

    private static final String TABLE_REL_KEY = "table_uuid";

    private TableRelUtils() {
    }

    public static <S extends TableRel> List<S> getTableRelInfos(String tableUuid, Class<S> tableRelClass) throws ConverterException {
        try {
            CrudMapper<S> crudMapper = CrudUtils.getCrudMapper(tableRelClass);
            return crudMapper.selectList(Wrappers.<S>query().eq(TABLE_REL_KEY, tableUuid));
        } catch (Throwable throwable) {
            throw new ConverterException(throwable);
        }
    }

    public static <S extends TableRel> S getTableRelInfo(String tableUuid, Class<S> tableRelClass) throws ConverterException {
        try {
            CrudMapper<S> crudMapper = CrudUtils.getCrudMapper(tableRelClass);
            return crudMapper.selectOne(Wrappers.<S>query().eq(TABLE_REL_KEY, tableUuid));
        } catch (Throwable throwable) {
            throw new ConverterException(throwable);
        }
    }

    /**
     * 保存表的关联信息
     *
     * @param collection 关联信息的集合
     * @param tableUuid  表实体的主键UUID
     * @param <T>        关联实体的类型
     * @throws Exception 数据库操作异常
     */
    @SuppressWarnings("unchecked")
    public static <T extends TableRel> void saveTableRelInfos(Collection<T> collection, String tableUuid) throws Exception {
        if (Objects.isNull(collection) || collection.isEmpty()) {
            return;
        }
        Class<T> entityClass = (Class<T>) collection.stream().findAny().get().getClass();
        collection.parallelStream().forEach(relEntity -> relEntity.setTableUuid(tableUuid));
        CrudMapper<T> crudMapper = CrudUtils.getCrudMapper(entityClass);
        crudMapper.saveBatch(collection);
    }


}
