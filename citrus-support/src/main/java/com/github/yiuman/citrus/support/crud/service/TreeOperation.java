package com.github.yiuman.citrus.support.crud.service;

import com.github.yiuman.citrus.support.crud.query.Query;
import com.github.yiuman.citrus.support.model.Tree;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 树形操作接口
 *
 * @author yiuman
 * @date 2020/4/15
 */
public interface TreeOperation<T extends Tree<K>, K extends Serializable> {

    /**
     * 根节点
     *
     * @return 节点点实例
     */
    T getRoot();

    /**
     * 获取代表深度的属性名 默认为deep
     *
     * @return 深度字段属性名
     */
    default String getDeepField() {
        return "deep";
    }

    /**
     * 获取代表父节点ID的属性名 默认为parent_id
     *
     * @return 父节点ID的属性名
     */
    default String getParentField() {
        return "parent_id";
    }

    /**
     * 获取代表左值的属性名 默认为left_value
     *
     * @return 左值的属性名
     */
    default String getLeftField() {
        return "left_value";
    }

    /**
     * 获取代表右值的属性名 默认为right_value
     *
     * @return 右值的属性名
     */
    default String getRightField() {
        return "right_value";
    }

    /**
     * 从新构建预遍历树
     *
     * @throws Exception 一般为数据库异常或反射异常
     */
    @Transactional(rollbackFor = Exception.class)
    void reInit() throws Exception;

    /**
     * 加载树
     *
     * @param isLazy 是否懒加载
     * @return 树
     * @throws Exception 一般为数据库异常或反射异常
     */
    T load(boolean isLazy) throws Exception;

    /**
     * 树查询
     *
     * @param query 查询条件
     * @return 树结构
     * @throws Exception 一般为数据库异常或反射异常
     */
    T treeQuery(Query query) throws Exception;

    /**
     * 加载某个节点下所有数据
     *
     * @param current 当前节点
     * @throws Exception 一般为数据库异常或反射异常
     */
    void load(T current) throws Exception;

    /**
     * 加载某个节点的下级节点
     *
     * @param current 当前节点
     * @param isLazy  是否懒加载
     * @throws Exception 一般为数据库异常或反射异常
     */
    void load(T current, boolean isLazy) throws Exception;

    /**
     * 根据父节点ID加载子列表
     *
     * @param parentKey 父节点ID
     * @return 子列表
     */
    List<T> loadByParent(K parentKey);

    /**
     * 移动
     *
     * @param current 当前节点
     * @param moveTo  移动到的节点
     * @throws Exception 一般为数据库异常或反射异常
     */
    @Transactional(rollbackFor = Exception.class)
    void move(T current, K moveTo) throws Exception;

    /**
     * 查询某个节点的后代
     *
     * @param current 当前节点
     * @return 获取当前节点的后代节点
     */
    List<T> children(T current);

    /**
     * 根据深度查询某个节点后代
     *
     * @param current 当前节点
     * @param deep    查询的深度
     * @return 子节点
     */
    List<T> children(T current, int deep);

    /**
     * 查询所有祖先节点
     *
     * @param current 当前节点
     * @return 祖先节点
     */
    List<T> parents(T current);

    /**
     * 查询对应高度的祖先节点
     *
     * @param current 当前节点
     * @param high    祖先节点的高度
     * @return 祖先节点
     */
    T parent(T current, int high);

    /**
     * 查询所有兄弟节点
     *
     * @param current 当前节点
     * @return 兄弟节点
     */
    List<T> siblings(T current);

}