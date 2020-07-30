package com.github.yiuman.citrus.system.service;

import com.github.yiuman.citrus.support.crud.service.BasePreOrderTreeService;
import com.github.yiuman.citrus.support.widget.TreeNode;
import com.github.yiuman.citrus.system.entity.Organization;
import org.springframework.stereotype.Service;

/**
 * @author yiuman
 * @date 2020/4/6
 */
@Service
public class OrganService extends BasePreOrderTreeService<Organization, Long> {

    public OrganService() {
    }

    /**
     * 获取机构选择控件
     *
     * @return 组织机构的TreeNode控件
     * @throws Exception 数据库异常
     */
    public TreeNode<Organization> getOrganTree(String label, String fieldKey, boolean multipleSelect) throws Exception {
        TreeNode<Organization> organizationTreeNode = new TreeNode<>(label, fieldKey, treeQuery(null));
        organizationTreeNode.setMultipleSelect(multipleSelect);
        organizationTreeNode.setModelKeyField(getKeyProperty());
        organizationTreeNode.setModelTextField("organName");
        return organizationTreeNode;
    }

}
