package com.leyou.upload.controller;

import com.leyou.upload.service.serviceimpl.UploadServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: 98050
 * Time: 2018-08-09 14:36
 * Feature:
 */
@RestController
@RequestMapping("upload")
public class UploadController {
    @Autowired
    private UploadServiceImpl uploadServiceImpl;

    /**
     * 图片上传
     * @param file
     * @return
     */
    @PostMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam("file")MultipartFile file){
        String url= this.uploadServiceImpl.upload(file);
        if(StringUtils.isBlank(url)){
            //url为空，证明上传失败
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(url);
    }
}
