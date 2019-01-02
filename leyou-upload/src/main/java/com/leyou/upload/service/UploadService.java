package com.leyou.upload.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: 98050
 * Time: 2018-08-09 14:44
 * Feature:
 */
public interface UploadService {
    /**
     * 文件上传
     * @param file
     * @return
     */
    String upload(MultipartFile file);
}
