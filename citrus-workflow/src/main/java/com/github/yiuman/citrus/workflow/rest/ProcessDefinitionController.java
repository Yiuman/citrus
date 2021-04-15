package com.github.yiuman.citrus.workflow.rest;

import com.github.yiuman.citrus.support.crud.rest.Operations;
import com.github.yiuman.citrus.support.crud.view.impl.PageTableView;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.support.utils.WebUtils;
import lombok.Data;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.zip.ZipInputStream;

/**
 * 流程定义相关Rest控制器
 * 注意！！！上传文件进行部署，若文件类型不为bpmn则不会生成act_re_procdef记录，只会生成act_re_deployment、act_ge_bytearray记录
 *
 * @author yiuman
 * @date 2020/12/10
 */
@RestController
@RequestMapping("/rest/procdef")
public class ProcessDefinitionController extends BaseWorkflowQueryController<ProcessDefinitionController.ProcessDefinitionInfo, String> {

    private static final Set<String> COMPRESSED_PACKAGE = new HashSet<String>() {{
        add("zip");
        add("bar");
    }};

    /**
     * 防止序列化报错，重写getIdentityLinks
     */
    static class ProcessDefinitionInfo extends ProcessDefinitionEntityImpl {

        @Override
        public List<IdentityLinkEntity> getIdentityLinks() {
            return null;
        }
    }

    @Data
    static class ProcessDefinitionQueryParams {

        String processDefinitionKeyLike;

        String processDefinitionNameLike;

        String processDefinitionCategoryLike;
    }


    public ProcessDefinitionController() {
        setParamClass(ProcessDefinitionQueryParams.class);
    }

    @Override
    protected Object createView() {
        PageTableView<Deployment> view = new PageTableView<>();
        view.addWidget("流程定义Key", "processDefinitionKeyLike");
        view.addWidget("名称", "processDefinitionNameLike");
        view.addWidget("目录", "processDefinitionCategoryLike");
        view.addHeader("ID", "id");
        view.addHeader("流程定义名称", "name");
        view.addHeader("版本号", "version");
        return view;
    }

    @Override
    public String getKeyQueryField() {
        return "processDefinitionId";
    }

    @Override
    protected Function<? super Object, ? extends ProcessDefinitionInfo> getTransformFunc() {
        return item -> {
            ProcessDefinitionInfo processDefinitionInfo = new ProcessDefinitionInfo();
            BeanUtils.copyProperties(item, processDefinitionInfo);
            return processDefinitionInfo;
        };
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
        DeploymentBuilder deployment = getProcessEngine().getRepositoryService().createDeployment();
        if (COMPRESSED_PACKAGE.contains(extension)) {
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
        getProcessEngine().getRepositoryService().deleteDeployment(key, true);
        return ResponseEntity.ok();
    }

    /**
     * 获取流程的资源文件
     *
     * @param processDefinitionId 流程定义ID
     * @param type                资源类型 默认/0 bpmn 1 png
     */
    @GetMapping("/resource/{processDefinitionId}")
    public void getProcessResource(@PathVariable String processDefinitionId, Integer type) throws IOException {
        RepositoryService repositoryService = getProcessEngine().getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);
        String resourceName = ObjectUtils.isEmpty(type) || type == 0
                ? processDefinition.getResourceName()
                : processDefinition.getDiagramResourceName();
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
        WebUtils.export(resourceAsStream, resourceName);
    }

}
