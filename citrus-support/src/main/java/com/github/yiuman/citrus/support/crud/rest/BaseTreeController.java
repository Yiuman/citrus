package com.github.yiuman.citrus.support.crud.rest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.TypeUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yiuman.citrus.support.crud.query.Query;
import com.github.yiuman.citrus.support.crud.service.BasePreOrderTreeService;
import com.github.yiuman.citrus.support.crud.service.BaseSimpleTreeService;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.crud.service.TreeCrudService;
import com.github.yiuman.citrus.support.crud.view.DataView;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.support.model.BasePreOrderTree;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.model.Tree;
import com.github.yiuman.citrus.support.utils.CrudUtils;
import com.github.yiuman.citrus.support.utils.WebUtils;
import org.springframework.web.bind.annotation.*;

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
        extends BaseCrudController<T, K> {

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
            if (superclass.isAssignableFrom(BasePreOrderTree.class)) {
                return CrudUtils.getCrudService(
                        modelClass,
                        (Class<K>) TypeUtil.getTypeArgument(getClass(), 1),
                        BasePreOrderTreeService.class
                );
            }

            return CrudUtils.getCrudService(
                    modelClass,
                    (Class<K>) TypeUtil.getTypeArgument(getClass(), 1),
                    BaseSimpleTreeService.class
            );
        } catch (Exception e) {
            return null;
        }

    }

    @GetMapping(Operations.VIEW)
    @SuppressWarnings("unchecked")
    @Override
    public ResponseEntity<?> getPageView(@RequestParam(defaultValue = PageAction.PAGE) String action, HttpServletRequest request) throws Exception {
        try {
            if (PageAction.TREE.equals(action)) {
                T treeEntity = treeRequest(request);
                List<T> treeList = ObjectUtil.isEmpty(treeEntity.getId())
                        ? (List<T>) treeEntity.getChildren()
                        : CollUtil.newArrayList(treeEntity);

                Page<T> treePage = Page.of(treeList);
                treePage.setItemKey(getCrudService().getKeyProperty());
                VIEW_DATA.set(treePage);
                Object view = createPageView();
                if (view instanceof DataView) {
                    ((DataView<Object>) view).setData(getViewData());
                }
                return ResponseEntity.ok(view);
            }
        } finally {
            VIEW_DATA.remove();
        }

        return super.getPageView(action, request);
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
