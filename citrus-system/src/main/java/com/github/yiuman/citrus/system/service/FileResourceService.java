package com.github.yiuman.citrus.system.service;

import cn.hutool.core.io.FileUtil;
import com.github.yiuman.citrus.support.crud.query.builder.QueryBuilders;
import com.github.yiuman.citrus.support.crud.service.BaseService;
import com.github.yiuman.citrus.support.file.FileStorageService;
import com.github.yiuman.citrus.system.entity.FileResource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * 文件资源管理
 *
 * @author yiuman
 * @date 2021/3/16
 */
@Service
@RequiredArgsConstructor
public class FileResourceService extends BaseService<FileResource, String> {

    private final FileStorageService fileStorageService;

    public String upload(String identify, MultipartFile file) throws Exception {

        //没有传文件标识即计算一次md5哈希值作为唯一标识
        if (ObjectUtils.isEmpty(identify)) {
            identify = makeIdentify(file.getInputStream());
        }
        FileResource fileResource = get(QueryBuilders.<FileResource>lambda().eq(FileResource::getIdentify, identify).toQuery());
        if (Objects.nonNull(fileResource)) {
            return fileResource.getFileId();
        }

        fileResource = new FileResource();
        fileResource.setIdentify(identify);
        String originalFilename = file.getOriginalFilename();

        String uploadFilePath = fileStorageService.save(FileUtil.getPrefix(originalFilename), file.getInputStream());
        fileResource.setFilename(originalFilename);
        fileResource.setPath(uploadFilePath);
        fileResource.setSize(file.getSize());
        fileResource.setFileType(FileUtil.getSuffix(originalFilename));
        return save(fileResource);
    }

    /**
     * 构造文件的标识
     *
     * @param inputStream 文件流
     * @return md5hex
     * @throws IOException 输入输出异常
     */
    private String makeIdentify(InputStream inputStream) throws IOException {
        return DigestUtils.md5Hex(StreamUtils.copyToByteArray(inputStream));
    }

    public InputStream getInputStream(FileResource fileResource) throws IOException {
        return fileStorageService.get(fileResource.getPath());
    }

}
