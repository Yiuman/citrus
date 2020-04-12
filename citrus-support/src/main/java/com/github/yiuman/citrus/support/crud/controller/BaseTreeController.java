package com.github.yiuman.citrus.support.crud.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.crud.service.TreeService;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.support.model.Tree;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 基础树结构控制器
 *
 * @author yiuman
 * @date 2020/4/9
 */
public abstract class BaseTreeController<S extends CrudService<T, K> & TreeService<T, K>, T extends Tree<K>, K>
        extends BaseCrudController<S, T, K> {

    private boolean isLazy = true;

    public BaseTreeController() {
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }

    /**
     * 加载树,支持查询
     */
    @GetMapping("/tree")
    public ResponseEntity<T> load(HttpServletRequest request) throws Exception {
        QueryWrapper<T> queryWrapper = queryWrapper(request);
        if (queryWrapper != null) {
            return ResponseEntity.ok(service.treeQuery(queryWrapper));
        }
        return ResponseEntity.ok(service.load(isLazy));
    }

    /**
     * 根据父级ID加载子列表
     *
     * @param parentKey 父ID
     */
    @GetMapping("/tree/{parentKey}")
    public ResponseEntity<List<T>> loadByParentKey(@PathVariable @NotNull K parentKey) {
        return ResponseEntity.ok(service.loadByParent(parentKey));
    }

    /**
     * 移动
     *
     * @param currentId 当前ID
     * @param moveToId  移动到的位置的Id
     */
    @PutMapping("/tree/move")
    public ResponseEntity<Void> move(@NotNull K currentId, @NotNull K moveToId) throws Exception {
        service.move(service.get(currentId), moveToId);
        return ResponseEntity.ok();
    }

    /**
     * 从新初始化左右值深度等
     */
    @PostMapping("/tree/init")
    public ResponseEntity<Void> reInit() throws Exception {
        service.reInit();
        return ResponseEntity.ok();
    }

}
