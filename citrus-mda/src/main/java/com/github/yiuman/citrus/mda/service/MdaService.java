package com.github.yiuman.citrus.mda.service;

import com.github.yiuman.citrus.mda.ddl.DdlProcessor;
import com.github.yiuman.citrus.mda.dml.DmlProcessor;
import com.github.yiuman.citrus.mda.entity.Table;
import com.github.yiuman.citrus.mda.exception.MdaException;
import com.github.yiuman.citrus.mda.meta.TableMeta;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.utils.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

/**
 * 动态模型逻辑服务接口
 *
 * @author yiuman
 * @date 2021/5/2
 */
public interface MdaService extends CrudService<Map<String, Object>, String> {


    /**
     * 获取DDL操作处理器
     *
     * @return DDL操作处理器实现
     */
    DdlProcessor getDdlProcessor();

    /**
     * 获取DML操作处理器
     *
     * @return DML操作处理器实现
     */
    DmlProcessor getDmlProcessor();

    /**
     * 获取表实体逻辑处理类
     *
     * @return 表实体逻辑处理类
     */
    TableEntityService getTableEntityService();

    /**
     * 模型ID的请求头KEY值
     */
    String MODEL_ID_TAG = "model_id";

    /**
     * 根据请求获取当前的模型ID
     *
     * @param request 当前请求
     * @return 动态模型ID
     */
    default String getModelId(HttpServletRequest request) {
        String modelId = Optional
                .ofNullable(request.getHeader(MODEL_ID_TAG))
                .orElse(request.getParameter(MODEL_ID_TAG));
        if (StringUtils.isEmpty(modelId)) {
            throw new MdaException("The model ID cannot be empty!");
        }
        return modelId;
    }

    /**
     * 根据请求获取当前的模型ID
     *
     * @return 动态模型ID
     */
    default String getModelId() {
        return getModelId(WebUtils.getRequest());
    }

    /**
     * 获取表的元信息
     *
     * @param request 当前请求
     * @return 元表
     */
    default TableMeta getTableMeta(HttpServletRequest request) {
        return entity2Meta(getModelId(request));
    }

    /**
     * 获取表的元信息
     *
     * @return 元表
     */
    default TableMeta getTableMeta() {
        return getTableMeta(WebUtils.getRequest());
    }

    /**
     * 获取实体表
     *
     * @return 实体表信息
     */
    default Table getTableEntity() {
        return getTableEntity(getModelId(WebUtils.getRequest()));
    }

    /**
     * 获取表实体
     *
     * @param tableId 表Id
     * @return 表实体
     */
    Table getTableEntity(String tableId);

    /**
     * 创建表
     *
     * @param tableId 表实体ID
     */
    void createTable(String tableId);

    /**
     * 根据表实体的UUID转化为表元数据
     *
     * @param uuid 表实体的UUID
     * @return 元数据模型
     */
    TableMeta entity2Meta(String uuid);

    /**
     * 根据表实体转化为表元数据
     *
     * @param table 表实体
     * @return 元数据模型
     */
    TableMeta entity2Meta(Table table);

}