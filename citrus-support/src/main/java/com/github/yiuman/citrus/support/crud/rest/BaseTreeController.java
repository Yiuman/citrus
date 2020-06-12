package com.github.yiuman.citrus.support.crud.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.crud.service.TreeCrudService;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.support.model.Tree;
import com.github.yiuman.citrus.support.model.TreeDisplay;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 基础树结构控制器
 *
 * @author yiuman
 * @date 2020/4/9
 */
public abstract class BaseTreeController<T extends Tree<K>, K extends Serializable>
        extends BaseCrudController<T, K> {

    private boolean isLazy = false;

    public BaseTreeController() {
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }

    /**
     * 获取树形CRUD的逻辑层实现类
     *
     * @return 实现了TreeCrudService的逻辑服务类
     */
    protected abstract TreeCrudService<T, K> getCrudService();

    @Override
    protected CrudService<T, K> getService() {
        return getCrudService();
    }

    protected TreeDisplay<T> createTree() throws Exception {
        TreeDisplay<T> treeDisplay = new TreeDisplay<>();
        treeDisplay.setItemKey(getService().getKeyProperty());
        return treeDisplay;
    }

    /**
     * 加载树,支持查询
     */
    @GetMapping("/tree")
    public ResponseEntity<TreeDisplay<T>> load(HttpServletRequest request) throws Exception {
        TreeDisplay<T> tree = this.createTree();
        QueryWrapper<T> queryWrapper = getQueryWrapper(request);
        if (queryWrapper != null) {
            tree.setTree(getCrudService().treeQuery(queryWrapper));
            return ResponseEntity.ok(tree);
        }

        tree.setTree(getCrudService().load(isLazy));
        tree.setLazy(isLazy);
        tree.setDialogView(createDialogView());
        return ResponseEntity.ok(tree);
    }

    /**
     * 根据父级ID加载子列表
     *
     * @param parentKey 父ID
     */
    @GetMapping("/tree/{parentKey}")
    public ResponseEntity<List<T>> loadByParentKey(@PathVariable @NotNull K parentKey) {
        return ResponseEntity.ok(getCrudService().loadByParent(parentKey));
    }

    /**
     * 移动
     *
     * @param currentId 当前ID
     * @param moveToId  移动到的位置的Id
     */
    @PutMapping("/tree/move")
    public ResponseEntity<Void> move(@NotNull K currentId, @NotNull K moveToId) throws Exception {
        getCrudService().move(getCrudService().get(currentId), moveToId);
        return ResponseEntity.ok();
    }

    /**
     * 从新初始化左右值深度等
     */
    @PostMapping("/tree/init")
    public ResponseEntity<Void> reInit() throws Exception {
        getCrudService().reInit();
        return ResponseEntity.ok();
    }

}
