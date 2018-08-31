package com.oxchains.themis.user.rest;

import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.param.FileVO;
import com.oxchains.themis.user.service.UploadService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ccl
 * @time 2018-07-02 17:27
 * @name UploadController
 * @desc:
 */
@RestController
@RequestMapping(value = "/tfile")
public class UploadController {
    @Resource
    private UploadService uploadService;

    @RequestMapping(value = "/upload")
    public RestResp upload(@ModelAttribute FileVO vo){
        return uploadService.upload(vo);
    }

    @GetMapping(value = "/download")
    public void download(HttpServletResponse response){
        uploadService.download(response);
    }
}
