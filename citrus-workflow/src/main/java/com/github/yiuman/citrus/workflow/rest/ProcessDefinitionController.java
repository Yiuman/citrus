package com.github.yiuman.citrus.workflow.rest;

import com.github.yiuman.citrus.support.crud.rest.Operations;
import com.github.yiuman.citrus.support.crud.view.impl.PageTableView;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import lombok.Data;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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
public class ProcessDefinitionController extends BaseWorkflowQueryController<ProcessDefinition, String> {

    public ProcessDefinitionController() {
        setParamClass(ProcessDefinitionQueryParams.class);
    }

    @Data
    static class ProcessDefinitionQueryParams {

        String processDefinitionKeyLike;

        String processDefinitionNameLike;

        String processDefinitionCategoryLike;
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

//    @Override
//    public Page<ProcessDefinition> page(HttpServletRequest request) throws Exception {
//        Page<ProcessDefinition> page = new Page<>();
//        WebUtils.requestDataBind(page, request);
//        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
//        page.setTotal(processDefinitionQuery.count());
//        page.setRecords(
//                getPageableDeployments(
//                        processDefinitionQuery,
//                        (ProcessDefinitionQueryParams) getQueryParams(request),
//                        (int) page.getCurrent(),
//                        (int) page.getSize()
//                )
//        );
//        page.setView(createView());
//        return page;
//    }

//    private List<ProcessDefinition> getPageableDeployments(ProcessDefinitionQuery query, ProcessDefinitionQueryParams params, int current, int pageSize) {
//        if (Objects.nonNull(params)) {
//            ReflectionUtils.doWithMethods(query.getClass(), method -> {
//                try {
//                    Field field = ReflectionUtils.findField(ProcessDefinitionQueryParams.class, method.getName());
//                    if (Objects.nonNull(field)) {
//                        Object paramValue = field.get(params);
//                        if (Objects.nonNull(paramValue)) {
//                            method.invoke(query, paramValue);
//                        }
//                    }
//                } catch (Exception ignore) {
//                }
//
//            }, method -> method.getName().endsWith("Like"));
//        }
//
//        return query.listPage(current, pageSize).stream().map(item -> {
//            ProcessDefinitionInfo processDefinitionInfo = new ProcessDefinitionInfo();
//            BeanUtils.copyProperties(item, processDefinitionInfo);
//            return processDefinitionInfo;
//        }).collect(Collectors.toList());
//    }

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
        getProcessEngine().getRepositoryService().deleteDeployment(key, true);
        return ResponseEntity.ok();
    }


    /**
     * 防止序列化报错，重写getIdentityLinks
     */
    static class ProcessDefinitionInfo extends ProcessDefinitionEntityImpl {

        @Override
        public List<IdentityLinkEntity> getIdentityLinks() {
            return null;
        }
    }

    @Override
    protected Function<ProcessDefinition, ? extends ProcessDefinition> getTransformFunc() {
        return item -> {
            ProcessDefinitionInfo processDefinitionInfo = new ProcessDefinitionInfo();
            BeanUtils.copyProperties(item, processDefinitionInfo);
            return processDefinitionInfo;
        };
    }
}
