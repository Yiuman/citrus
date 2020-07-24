package com.github.yiuman.citrus.system.service;

import com.github.yiuman.citrus.support.utils.WebUtils;
import com.github.yiuman.citrus.system.entity.Organization;
import com.github.yiuman.citrus.system.entity.Resource;
import com.github.yiuman.citrus.system.entity.ScopeDefine;
import com.github.yiuman.citrus.system.entity.User;
import com.github.yiuman.citrus.system.enums.ScopeType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * 数据范围处理逻辑实现类
 *
 * @author yiuman
 * @date 2020/7/23
 */
@Component
public class DataRangeServiceImpl implements DataRangeService {

    private final RbacMixinService rbacMixinService;

    public DataRangeServiceImpl(RbacMixinService rbacMixinService) {
        this.rbacMixinService = rbacMixinService;
    }

    @Override
    public Collection<Long> getDeptIds() {
        UserService userService = rbacMixinService.getUserService();
        Optional<User> currentUser = userService.getCurrentUser();
        if (!currentUser.isPresent()) {
            return Collections.singletonList(-1L);
        }
        //获取当前请求的对应的RequestMapping路径
        try {
            HttpServletRequest request = WebUtils.getRequest();
            String mvcDefineMapping = WebUtils.getRequestMapping(request);
            if (!StringUtils.isEmpty(mvcDefineMapping)) {
                //没找到配置资源证明没有配置资源，需要要数据范围
                Resource resource = rbacMixinService.getResourceService().selectByUri(mvcDefineMapping, request.getMethod());
                if (Objects.isNull(resource)) {
                    return null;
                }

                //查当前资源的数据范围定义
                List<ScopeDefine> scopeDefines = rbacMixinService
                        .getAuthorityService()
                        .getScopeDefinesByResourceId(resource.getResourceId());

                if (Objects.isNull(scopeDefines)) {
                    return null;
                }

                //下面使用并行流处理，这时使用的是线程安全的Set
                Set<Long> authDeptIds = new CopyOnWriteArraySet<>();
                OrganService organService = rbacMixinService.getOrganService();
                //这里处理数据范围
                scopeDefines.parallelStream().forEach(scopeDefine -> {
                    Long scopeOrganId = scopeDefine.getOrganId();
                    //获取范围定义部门
                    Set<Long> currentOrganIds = new HashSet<>();
                    ScopeType[] scopeTypes = scopeDefine.getScopeTypes();

                    //数据范围定义的部门ID>0时为正常情况，按照正常逻辑处理
                    if (scopeOrganId > 0) {
                        //根据数据范围类型算出数据范围定义的部门ID集合
                        Organization organization = organService.get(scopeDefine.getOrganId());
                        for (ScopeType scopeType : scopeTypes) {
                            currentOrganIds.addAll(getScopeOrganIds(scopeType, organization));
                        }
                    } else {
                        List<Organization> currentUserOrgans = userService.getCurrentUserOrgans();
                        //数据范围定义的部门ID=0时表示当前用户的部门，遍历当前用户部门处理
                        if (scopeOrganId == 0) {
                            currentUserOrgans.parallelStream().forEach(organization -> {
                                for (ScopeType scopeType : scopeTypes) {
                                    currentOrganIds.addAll(getScopeOrganIds(scopeType, organization));
                                }
                            });
                        } else {
                            //数据范围定义的部门ID<0时为，-(scopeOrganId)的级别，则部门树的深度
                            int deep = Math.toIntExact(-scopeOrganId);
                            currentUserOrgans.parallelStream().forEach(organization -> {
                                for (ScopeType scopeType : scopeTypes) {
                                    currentOrganIds.addAll(getScopeOrganIds(scopeType, organService.parent(organization, deep)));
                                }
                            });
                        }

                    }

                    //获取到的当前的数据范围
                    if (!CollectionUtils.isEmpty(currentOrganIds)) {
                        //包含
                        if (scopeDefine.getScopeRule() == null || scopeDefine.getScopeRule() == 0) {
                            authDeptIds.addAll(currentOrganIds);
                        }
                        //排除
                        else {
                            authDeptIds.removeAll(currentOrganIds);
                        }
                    }
                });

                return authDeptIds;
            }
        } catch (Exception ignore) {
        }

        return null;
    }

    private Collection<Long> getScopeOrganIds(ScopeType scopeType, Organization organization) {
        OrganService organService = rbacMixinService.getOrganService();
        switch (scopeType) {
            //找子部门
            case INCLUDE_SUB:
                List<Organization> children = organService.children(organization);
                return children.parallelStream()
                        .map(Organization::getOrganId)
                        .collect(Collectors.toSet());
            //找父部门
            case INCLUDE_SUP:
                List<Organization> parents = organService.parents(organization);
                return parents.parallelStream()
                        .map(Organization::getOrganId)
                        .collect(Collectors.toSet());
            default:
                return Collections.singleton(organization.getOrganId());
        }
    }
}
