package com.github.yiuman.citrus.system.service;

import com.github.yiuman.citrus.support.crud.service.BaseService;
import com.github.yiuman.citrus.system.entity.FileResource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yiuman
 * @date 2021/3/16
 */
public class FileResourceService extends BaseService<FileResource, String> {

    public FileResourceService() {
    }

    public String upload(MultipartFile file) throws Exception {
        FileResource fileResource = new FileResource();
        fileResource.setMd5("");
        String originalFilename = file.getOriginalFilename();
        fileResource.setFilename(originalFilename);
        //todo 这里根据配置做处理  可为本地路径，ftp，云等
        fileResource.setPath("");
        fileResource.setSize(file.getSize());
        fileResource.setFileType(originalFilename.substring(originalFilename.lastIndexOf(".")));
        return save(fileResource);
    }

}
