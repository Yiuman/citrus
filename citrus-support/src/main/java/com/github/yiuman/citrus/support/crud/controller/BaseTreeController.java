package com.github.yiuman.citrus.support.crud.controller;

import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.crud.service.TreeService;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.support.model.Tree;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yiuman
 * @date 2020/4/9
 */
public class BaseTreeController<S extends CrudService<T, K> & TreeService<T, K>, T extends Tree<K>, K>
        extends BaseCrudController<S, T, K> {

    private boolean isLazy = true;

    public BaseTreeController() {
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }

    @GetMapping("/tree")
    public ResponseEntity<T> load() throws Exception {
        return ResponseEntity.ok(service.load(isLazy));
    }

    @GetMapping("/tree/{parentKey}")
    public ResponseEntity<List<T>> loadByParentKey(@PathVariable @NotNull K parentKey) {
        return ResponseEntity.ok(service.loadByParent(parentKey));
    }

    @PutMapping("/tree/move")
    public ResponseEntity<Void> move(@NotNull K currentId, @NotNull K moveToId) throws Exception {
        service.move(service.get(currentId), moveToId);
        return ResponseEntity.ok();
    }

    @PostMapping("/tree/init")
    public ResponseEntity<Void> reInit() throws Exception {
        service.reInit();
        return ResponseEntity.ok();
    }

}
