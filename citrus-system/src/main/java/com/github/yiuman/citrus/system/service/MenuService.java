package com.github.yiuman.citrus.system.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.crud.rest.BaseTreeController;
import com.github.yiuman.citrus.support.crud.rest.CrudRestful;
import com.github.yiuman.citrus.support.crud.service.BaseSimpleTreeService;
import com.github.yiuman.citrus.system.dto.ResourceDto;
import com.github.yiuman.citrus.system.entity.Resource;
import com.github.yiuman.citrus.system.mapper.ResourceMapper;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 菜单树逻辑层
 *
 * @author yiuman
 * @date 2020/5/22
 */
@Service
public class MenuService extends BaseSimpleTreeService<Resource, Long> {

    private final ApplicationContext applicationContext;

    private final ResourceService resourceService;

    private final ResourceMapper resourceMapper;

    public MenuService(ApplicationContext applicationContext, ResourceService resourceService, ResourceMapper resourceMapper) {
        this.applicationContext = applicationContext;
        this.resourceService = resourceService;
        this.resourceMapper = resourceMapper;
    }

    @Override
    protected BaseMapper<Resource> getMapper() {
        return resourceMapper;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public List<Resource> list(Wrapper<Resource> wrapper) {
        if (wrapper == null) {
            wrapper = new QueryWrapper<>();
        }
        //菜单为0
        ((QueryWrapper) wrapper).eq("type", 0);
        return super.list(wrapper);
    }

    @Override
    public Resource getRoot() {
        Resource root = super.getRoot();
        root.setResourceName("系统菜单");
        return root;
    }

    @Override
    public boolean beforeSave(Resource entity) throws Exception {
        entity.setType(0);
        return true;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void afterSave(Resource entity) {
        boolean isCrudRest = false;
        boolean isAssignableFromTreeRest = false;
        //若是CrudRestful则保存操作资源，则增删改查资源
        Map<String, CrudRestful> crudRestfuls = applicationContext.getBeansOfType(CrudRestful.class);
        Set<Map.Entry<String, CrudRestful>> entries = crudRestfuls.entrySet();
        for (Map.Entry<String, CrudRestful> entry : entries) {
            Class<? extends CrudRestful> restBeanClass = (Class<? extends CrudRestful>) AopUtils.getTargetClass(entry.getValue());
            RequestMapping annotation = restBeanClass.getAnnotation(RequestMapping.class);
            if (annotation != null && Arrays.asList(annotation.value()).contains(entity.getPath())) {
                isCrudRest = true;
                isAssignableFromTreeRest = restBeanClass.isAssignableFrom(BaseTreeController.class);
                break;
            }
        }

        if (isCrudRest) {
            createCrudDefaultResource(entity);

            if (isAssignableFromTreeRest) {
                createTreeDefaultResource(entity);
            }
        }

    }

    /**
     * 根据菜单主键获取操作资源
     *
     * @param key 菜单主键
     * @return 操作资源列表
     */
    public List<Resource> getOperationByKey(Long key) {
        return super.list(Wrappers.<Resource>query().eq(getParentField(), key).eq("type", 2));
    }

    /**
     * 保存菜单默认的资源
     *
     * @param menu 当前菜单
     */
    private void createCrudDefaultResource(Resource menu) {
        final Integer resourceType = 2;
        resourceService.batchSave(Arrays.asList(
                new ResourceDto("列表", resourceType, menu.getId(), menu.getPath(), "GET"),
                new ResourceDto("保存", resourceType, menu.getId(), menu.getPath(), "POST"),
                new ResourceDto("查看", resourceType, menu.getId(), String.format("%s/{key}", menu.getPath()), "GET"),
                new ResourceDto("删除", resourceType, menu.getId(), String.format("%s/{key}", menu.getPath()), "DELETE"),
                new ResourceDto("批量删除", resourceType, menu.getId(), String.format("%s/batch_delete", menu.getPath()), "POST"),
                new ResourceDto("导入", resourceType, menu.getId(), String.format("%s/import", menu.getPath()), "POST"),
                new ResourceDto("导出", resourceType, menu.getId(), String.format("%s/export", menu.getPath()), "GET")
        ));
    }

    /**
     * 保存树形菜单资源的默认资源
     *
     * @param menu 当前树形菜单资源
     */
    private void createTreeDefaultResource(Resource menu) {
        final Integer resourceType = 2;
        resourceService.batchSave(Arrays.asList(
                new ResourceDto("加载树", resourceType, menu.getId(), String.format("%s/tree", menu.getPath()), "GET"),
                new ResourceDto("加载子节点", resourceType, menu.getId(), String.format("%s//tree/{parentKey}", menu.getPath()), "GET"),
                new ResourceDto("移动", resourceType, menu.getId(), String.format("%s/tree/move", menu.getPath()), "POST"),
                new ResourceDto("初始化", resourceType, menu.getId(), String.format("%s/tree/init", menu.getPath()), "POST")
        ));
    }
}
