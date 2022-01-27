package com.github.yiuman.citrus.mda.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.yiuman.citrus.mda.meta.DeleteMeta;
import com.github.yiuman.citrus.mda.meta.SaveMeta;
import com.github.yiuman.citrus.mda.meta.TableMeta;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * DML相关操作Mapper
 *
 * @author yiuman
 * @date 2021/4/27
 */
public interface DmlMapper {

    /**
     * 插入数据
     *
     * @param saveMeta 实体元
     * @return 影响行数
     */
    int insert(SaveMeta saveMeta);

    /**
     * 更新数据
     *
     * @param saveMeta      实体元
     * @param updateWrapper where条件
     * @return 影响行数
     */
    int update(@Param("meta") SaveMeta saveMeta, @Param(Constants.WRAPPER) Wrapper<?> updateWrapper);

    /**
     * 删除表数据
     *
     * @param deleteMeta   删除的元数据对象
     * @param queryWrapper 查询条件
     * @return 影响行数
     */
    int delete(@Param("meta") DeleteMeta deleteMeta, @Param(Constants.WRAPPER) Wrapper<?> queryWrapper);

    /**
     * 列表查询
     *
     * @param meta         表元信息
     * @param queryWrapper 查询条件
     * @return 符合条件的表格数据Map集合
     */
    List<Map<String, Object>> selectList(@Param("meta") TableMeta meta, @Param(Constants.WRAPPER) Wrapper<?> queryWrapper);

    /**
     * DML分页查询
     *
     * @param page         分页
     * @param meta         表元信息
     * @param queryWrapper 查询条件
     * @param <E>          分页泛型
     * @return 分页的mapper数据
     */
    <E extends IPage<Map<String, Object>>> E selectPage(E page, @Param("meta") TableMeta meta, @Param(Constants.WRAPPER) Wrapper<?> queryWrapper);
}