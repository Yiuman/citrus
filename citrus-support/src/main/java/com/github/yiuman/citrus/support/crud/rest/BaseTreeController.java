package com.github.yiuman.citrus.support.crud.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.crud.service.TreeCrudService;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.support.model.Tree;
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

    private boolean isLazy = true;

    public BaseTreeController() {
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }

    protected abstract TreeCrudService<T, K> getCrudService();

    @Override
    protected CrudService<T, K> getService() {
        return getCrudService();
    }

    /**
     * 加载树,支持查询
     */
    @GetMapping("/tree")
    public ResponseEntity<T> load(HttpServletRequest request) throws Exception {
        QueryWrapper<T> queryWrapper = getQueryWrapper(request);
        if (queryWrapper != null) {
            return ResponseEntity.ok(getCrudService().treeQuery(queryWrapper));
        }
        return ResponseEntity.ok(getCrudService().load(isLazy));
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
