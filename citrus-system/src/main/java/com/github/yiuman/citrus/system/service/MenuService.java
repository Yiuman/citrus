package com.github.yiuman.citrus.system.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.support.crud.rest.BaseTreeController;
import com.github.yiuman.citrus.support.crud.rest.Operations;
import com.github.yiuman.citrus.support.crud.rest.QueryRestful;
import com.github.yiuman.citrus.support.crud.service.BaseSimpleTreeService;
import com.github.yiuman.citrus.system.dto.ResourceDto;
import com.github.yiuman.citrus.system.entity.Resource;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

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

    public MenuService(ApplicationContext applicationContext, ResourceService resourceService) {
        this.applicationContext = applicationContext;
        this.resourceService = resourceService;
    }

    @Override
    public List<Resource> list() {
        return list(null);
    }

    @Override
    public List<Resource> list(Wrapper<Resource> wrapper) {
        if (wrapper == null) {
            wrapper = Wrappers.query();
        }
        //菜单为0
        ((QueryWrapper<Resource>) wrapper).lambda().eq(Resource::getType, 0);
        return super.list(wrapper);
    }

    @Override
    public Resource getRoot() {
        Resource root = super.getRoot();
        root.setResourceName("系统菜单");
        return root;
    }

    @Override
    public boolean beforeSave(Resource entity) {
        entity.setType(0);
        return true;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void afterSave(Resource entity) {
        //若是CrudRestful则保存操作资源，则增删改查资源
        Map<String, QueryRestful> crudRestfulMap = applicationContext.getBeansOfType(QueryRestful.class);
        final AtomicReference<Class<? extends QueryRestful>> targetClass = new AtomicReference<>();
        crudRestfulMap.values().parallelStream().forEach(item -> {
            Class<? extends QueryRestful> restClass = (Class<? extends QueryRestful>) AopUtils.getTargetClass(item);
            RequestMapping annotation = restClass.getAnnotation(RequestMapping.class);
            boolean isMapping = annotation != null && Arrays.asList(annotation.value()).contains(entity.getPath());
            if (isMapping) {
                targetClass.set(restClass);
            }
        });
        Class<? extends QueryRestful> restBeanClass = targetClass.get();
        if (Objects.nonNull(restBeanClass)) {
            resourceService.remove(Wrappers.<ResourceDto>lambdaQuery().eq(ResourceDto::getParentId, entity.getResourceId()));
            createQueryDefaultResource(entity);

            if (BaseCrudController.class.isAssignableFrom(restBeanClass)) {
                createCrudDefaultResource(entity);
            }

            if (BaseTreeController.class.isAssignableFrom(restBeanClass)) {
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
        return super.list(Wrappers.<Resource>query().eq(getParentField(), key).lambda().eq(Resource::getType, 2));
    }

    /**
     * 保存菜单默认的资源
     *
     * @param menu 当前菜单
     */
    private void createCrudDefaultResource(Resource menu) {
        final Integer resourceType = 2;
        resourceService.batchSave(Arrays.asList(
                new ResourceDto("新增", resourceType, menu.getId(), menu.getPath(), HttpMethod.POST.name(), Operations.Code.ADD),
                new ResourceDto("编辑", resourceType, menu.getId(), menu.getPath(), HttpMethod.POST.name(), Operations.Code.EDIT),
                new ResourceDto("删除", resourceType, menu.getId(), menu.getPath() + Operations.DELETE, HttpMethod.DELETE.name(), Operations.Code.DELETE),
                new ResourceDto("批量删除", resourceType, menu.getId(), menu.getPath() + Operations.BATCH_DELETE, HttpMethod.POST.name(), Operations.Code.BATCH_DELETE),
                new ResourceDto("导入", resourceType, menu.getId(), menu.getPath() + Operations.IMPORT, HttpMethod.POST.name(), Operations.Code.IMPORT)
        ));
    }

    private void createQueryDefaultResource(Resource menu) {

        final Integer resourceType = 2;
        resourceService.batchSave(Arrays.asList(
                new ResourceDto("列表", resourceType, menu.getId(), menu.getPath(), HttpMethod.GET.name(), Operations.Code.LIST),
                new ResourceDto("查看", resourceType, menu.getId(), menu.getPath() + Operations.GET, HttpMethod.GET.name(), Operations.Code.GET),
                new ResourceDto("导出", resourceType, menu.getId(), menu.getPath() + Operations.EXPORT, HttpMethod.GET.name(), Operations.Code.EXPORT)
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
                new ResourceDto("加载树", resourceType, menu.getId(), menu.getPath() + Operations.Tree.TREE, HttpMethod.GET.name()),
                new ResourceDto("加载子节点", resourceType, menu.getId(), menu.getPath() + Operations.Tree.GET_BY_PARENT, HttpMethod.GET.name()),
                new ResourceDto("移动", resourceType, menu.getId(), menu.getPath() + Operations.Tree.MOVE, HttpMethod.POST.name(), Operations.Code.MOVE),
                new ResourceDto("初始化", resourceType, menu.getId(), menu.getPath() + Operations.Tree.INIT, HttpMethod.POST.name())
        ));
    }
}
