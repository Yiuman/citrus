package com.github.yiuman.citrus.mda.dml;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.yiuman.citrus.mda.exception.DmlException;
import com.github.yiuman.citrus.mda.mapper.DmlMapper;
import com.github.yiuman.citrus.mda.meta.DeleteMeta;
import com.github.yiuman.citrus.mda.meta.SaveMeta;
import com.github.yiuman.citrus.mda.meta.TableMeta;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

/**
 * DML处理器接口
 *
 * @author yiuman
 * @date 2021/4/27
 */
public interface DmlProcessor {

    /**
     * 获取SqlSession工厂
     *
     * @param namespace 命名空间
     * @return SqlSessionFactory
     */
    SqlSessionFactory getSqlSessionFactory(String namespace);

    /**
     * 获取DmlMapper
     *
     * @param namespace 命苦空间
     * @return 所属命名空间的DmlMapper
     */
    default DmlMapper getDmlMapper(String namespace) {
        return getSqlSessionFactory(namespace).openSession().getMapper(DmlMapper.class);
    }

    /**
     * 保存数据
     *
     * @param meta 插入元模型
     * @return 是否保存成功
     */
    default boolean insert(SaveMeta meta) {
        return getDmlMapper(meta.getNamespace()).insert(meta) >= 0;
    }

    /**
     * 更新数据
     *
     * @param meta    更新元模型
     * @param wrapper 查询条件
     * @return 是否更新成功 true/false
     * @throws DmlException dml操作异常
     */
    default boolean update(SaveMeta meta, Wrapper<?> wrapper) throws DmlException {
        if (wrapper.isEmptyOfNormal()) {
            throw new DmlException("Remove condition cannot be null");
        }
        return getDmlMapper(meta.getNamespace()).update(meta, wrapper) >= 0;
    }

    /**
     * 删除某表的数据
     *
     * @param meta    删除的元模型
     * @param wrapper 删除条件
     * @return 是否删除成功
     * @throws DmlException dml操作异常
     */
    default boolean remove(DeleteMeta meta, Wrapper<?> wrapper) throws DmlException {
        if (wrapper.isEmptyOfNormal()) {
            throw new DmlException("Remove condition cannot be null");
        }
        return getDmlMapper(meta.getNamespace()).delete(meta, wrapper) >= 0;
    }

    /**
     * 默认的列表查询
     *
     * @param meta         表元信息
     * @param queryWrapper 查询条件
     * @return 符合条件的数据Map集合
     */
    default List<Map<String, Object>> list(TableMeta meta, Wrapper<?> queryWrapper) {
        return getDmlMapper(meta.getNamespace()).selectList(meta, queryWrapper);
    }


    /**
     * 翻页查询
     *
     * @param page         翻页对象
     * @param meta         表原信息
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @return 分页对象
     */
    default <E extends IPage<Map<String, Object>>> E page(E page, TableMeta meta, Wrapper<?> queryWrapper) {
        return getDmlMapper(meta.getNamespace()).selectPage(page, meta, queryWrapper);
    }


}