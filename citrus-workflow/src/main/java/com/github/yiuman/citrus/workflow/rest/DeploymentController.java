package com.github.yiuman.citrus.workflow.rest;

import com.github.yiuman.citrus.support.crud.rest.BaseQueryController;
import com.github.yiuman.citrus.support.crud.rest.Operations;
import com.github.yiuman.citrus.support.crud.view.impl.PageTableView;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.utils.WebUtils;
import lombok.Data;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipInputStream;

/**
 * 流程部署相关Rest控制器
 *
 * @author yiuman
 * @date 2020/12/10
 */
@RestController
@RequestMapping("/rest/workflow_definition")
public class DeploymentController extends BaseQueryController<Deployment, String> {

    private final RepositoryService repositoryService;

    public DeploymentController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
        setParamClass(DeploymentQueryParams.class);
    }

    @Data
    static class DeploymentQueryParams {

        String deploymentKeyLike;

        String deploymentNameLike;

        String deploymentCategoryLike;

        String deploymentTenantIdLike;

        String deploymentDefinitionKeyLike;
    }

    @Override
    protected Object createView() throws Exception {
        PageTableView<Deployment> view = new PageTableView<>();
        view.addWidget("流程部署Key", "deploymentKeyLike");
        view.addWidget("名称", "deploymentNameLike");
        view.addWidget("目录", "deploymentCategoryLike");
        view.addWidget("租户ID", "deploymentTenantIdLike");
        view.addWidget("流程部署定义Key", "deploymentDefinitionKeyLike");
        return view;
    }

    @Override
    public Page<Deployment> page(HttpServletRequest request) throws Exception {
        Page<Deployment> page = new Page<>();
        WebUtils.requestDataBind(page, request);
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        page.setTotal(deploymentQuery.count());
        page.setRecords(
                getPageableDeployments(
                        deploymentQuery,
                        (DeploymentQueryParams) getQueryParams(request),
                        (int) page.getCurrent(),
                        (int) page.getSize()
                )
        );
        page.setView(createView());
        return page;
    }

    private List<Deployment> getPageableDeployments(DeploymentQuery query, DeploymentQueryParams params, int current, int pageSize) {
        if (Objects.nonNull(params)) {
            ReflectionUtils.doWithMethods(query.getClass(), method -> {
                try {
                    Field field = ReflectionUtils.findField(DeploymentQueryParams.class, method.getName());
                    if (Objects.nonNull(field)) {
                        Object paramValue = field.get(params);
                        if (Objects.nonNull(paramValue)) {
                            method.invoke(query, paramValue);
                        }
                    }
                } catch (Exception ignore) {
                }

            }, method -> method.getName().endsWith("Like"));
        }
        return query.listPage(current, pageSize);
    }

    /**
     * 流程部署
     *
     * @param file bpmn文件或zip
     * @return 部署实体
     * @throws IOException IO异常
     */
    @PostMapping("/deploy")
    public ResponseEntity<String> deploy(@RequestBody MultipartFile file) throws IOException {
        // 获取上传的文件名
        String fileName = file.getOriginalFilename();
        // 得到输入流（字节流）对象
        InputStream fileInputStream = file.getInputStream();
        // 文件的扩展名
        String extension = FilenameUtils.getExtension(fileName);
        // zip或者bar类型的文件用ZipInputStream方式部署
        DeploymentBuilder deployment = repositoryService.createDeployment();
        if ("zip".equals(extension) || "bar".equals(extension)) {
            ZipInputStream zip = new ZipInputStream(fileInputStream);
            deployment.addZipInputStream(zip);
        } else {
            // 其他类型的文件直接部署
            deployment.addInputStream(fileName, fileInputStream);
        }
        Deployment result = deployment.deploy();
        return ResponseEntity.ok(result.getId());
    }

    @DeleteMapping(Operations.DELETE)
    public ResponseEntity<Void> delete(String key) {
        repositoryService.deleteDeployment(key, true);
        return ResponseEntity.ok();
    }

}
