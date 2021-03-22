package com.github.yiuman.citrus.system.rest;

import com.github.yiuman.citrus.security.authorize.Authorize;
import com.github.yiuman.citrus.support.crud.rest.BaseQueryRestful;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.utils.WebUtils;
import com.github.yiuman.citrus.system.entity.FileResource;
import com.github.yiuman.citrus.system.hook.HasLoginHook;
import com.github.yiuman.citrus.system.service.FileResourceService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author yiuman
 * @date 2021/3/21
 */
@Authorize(HasLoginHook.class)
@RestController
@RequestMapping("/rest/files")
public class FileResourceController extends BaseQueryRestful<FileResource, String> {

    private final FileResourceService fileResourceService;

    public FileResourceController(FileResourceService fileResourceService) {
        this.fileResourceService = fileResourceService;
    }

    @Override
    protected CrudService<FileResource, String> getService() {
        return fileResourceService;
    }


    @PostMapping("/upload")
    public String upload(String identify, MultipartFile file) throws Exception {
        return fileResourceService.upload(identify, file);
    }

    @GetMapping("/{key}")
    public void download(@PathVariable String key) throws IOException {
        FileResource fileResource = get(key);
        WebUtils.export(fileResourceService.getInputStream(fileResource), fileResource.getFilename());

    }
}
