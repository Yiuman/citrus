package com.github.yiuman.citrus.support.model;

import com.baomidou.mybatisplus.annotation.TableField;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 基础树形结构
 *
 * @author yiuman
 * @date 2020/5/22
 */
public abstract class BaseTree<T extends BaseTree<T, K>, K> implements Tree<K> {

    @TableField(exist = false)
    private List<T> children;

    public BaseTree() {
    }

    @Override
    public boolean isLeaf() {
        return CollectionUtils.isEmpty(getChildren());
    }

    @Override
    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }
}
