package com.github.yiuman.citrus.support.crud.rest;

import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yiuman.citrus.support.crud.query.Query;
import com.github.yiuman.citrus.support.crud.service.BasePreOrderTreeService;
import com.github.yiuman.citrus.support.crud.service.BaseSimpleTreeService;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.crud.service.TreeCrudService;
import com.github.yiuman.citrus.support.crud.view.TreeView;
import com.github.yiuman.citrus.support.crud.view.TreeViewable;
import com.github.yiuman.citrus.support.crud.view.ViewHelper;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.support.model.BasePreOrderTree;
import com.github.yiuman.citrus.support.model.Tree;
import com.github.yiuman.citrus.support.utils.CrudUtils;
import com.github.yiuman.citrus.support.utils.WebUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 基础树结构控制器
 *
 * @param <T> 实体类型
 * @param <K> 主键类型
 * @author yiuman
 * @date 2020/4/9
 */
public abstract class BaseTreeController<T extends Tree<K>, K extends Serializable>
        extends BaseCrudController<T, K> implements TreeViewable<T> {

    private boolean isLazy = false;

    public BaseTreeController() {
    }

    protected void setLazy(boolean lazy) {
        isLazy = lazy;
    }

    /**
     * 获取树形CRUD的逻辑层实现类
     *
     * @return 实现了TreeCrudService的逻辑服务类
     */
    @SuppressWarnings("unchecked")
    protected TreeCrudService<T, K> getCrudService() {
        Class<? super T> superclass = modelClass.getSuperclass();
        try {
            return superclass.isAssignableFrom(BasePreOrderTree.class)
                    ? CrudUtils.getCrudService(
                    modelClass,
                    (Class<K>) ReflectionKit.getSuperClassGenericType(getClass(), 1),
                    BasePreOrderTreeService.class)
                    : CrudUtils.getCrudService(
                    modelClass,
                    (Class<K>) ReflectionKit.getSuperClassGenericType(getClass(), 1),
                    BaseSimpleTreeService.class);
        } catch (Exception e) {
            return null;
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public <VIEW extends TreeView<T>> VIEW showTreeView(T data) {
        return (VIEW) ViewHelper.createTreeView(this, data);
    }

    @GetMapping(Operations.Tree.VIEW)
    public ResponseEntity<?> getTreeView(HttpServletRequest request) throws Exception {
        T treeData = treeRequest(request);
        Object treeView = showTreeView(treeData);
        return ResponseEntity.ok(Objects.nonNull(treeView) ? treeView : treeData);
    }

    @Override
    protected CrudService<T, K> getService() {
        return getCrudService();
    }

    /**
     * 加载树,支持查询
     */
    @GetMapping(Operations.Tree.TREE)
    public ResponseEntity<T> load(HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(treeRequest(request));
    }

    protected T treeRequest(HttpServletRequest request) throws Exception {
        Query query = getQueryCondition(request);
        return Objects.nonNull(query)
                ? getCrudService().treeQuery(query)
                : getCrudService().load(isLazy);
    }

    /**
     * 根据父级ID加载子列表
     *
     * @param parentKey 父ID
     */
    @GetMapping(Operations.Tree.GET_BY_PARENT)
    public ResponseEntity<List<T>> loadByParentKey(@PathVariable @NotNull K parentKey) {
        return ResponseEntity.ok(getCrudService().loadByParent(parentKey));
    }

    /**
     * 移动
     *
     * @param currentId 当前ID
     * @param moveToId  移动到的位置的Id
     */
    @PutMapping(Operations.Tree.MOVE)
    public ResponseEntity<Void> move(@NotNull K currentId, @NotNull K moveToId) throws Exception {
        getCrudService().move(getCrudService().get(currentId), moveToId);
        return ResponseEntity.ok();
    }

    /**
     * 从新初始化左右值深度等
     */
    @PostMapping(Operations.Tree.INIT)
    public ResponseEntity<Void> reInit() throws Exception {
        getCrudService().reInit();
        return ResponseEntity.ok();
    }

    @Override
    public void exp(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ResponseEntity<T> load = this.load(request);
        String fileName = WebUtils.getRequestParam("fileName");
        if (StringUtils.isBlank(fileName)) {
            fileName = String.valueOf(System.currentTimeMillis());
        }
        WebUtils.exportJson(response, load.getData(), fileName);
    }
}
