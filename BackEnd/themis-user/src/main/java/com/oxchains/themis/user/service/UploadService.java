package com.oxchains.themis.user.service;

import com.oxchains.basicService.files.tfsService.TFSConsumer;
import com.oxchains.themis.common.i18n.I18NConst;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.param.FileVO;
import com.oxchains.themis.common.util.RandomUtil;
import com.oxchains.themis.common.util.UuidUtils;
import com.oxchains.themis.repo.entity.user.User;
import com.oxchains.themis.repo.entity.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @author ccl
 * @time 2018-07-02 17:27
 * @name UploadService
 * @desc:
 */
@Slf4j
@Service
public class UploadService {
    @Resource
    private TFSConsumer tfsConsumer;

    public RestResp upload(FileVO vo){
        if(null == vo){
            return RestResp.fail("文件信息不能为空");
        }
        MultipartFile file = vo.getFile();
        if(null != file) {
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            Long uid = RandomUtil.getRandomNumber(8);
            String newFileName = tfsConsumer.saveTfsFile(file, uid);
            if (null == newFileName) {
                return RestResp.fail("上传文件失败");
            }
            vo.setName(newFileName);
            vo.setFile(null);
            return RestResp.success("上传文件成功",vo);
        }
        return RestResp.fail("上传文件失败");
    }

    public void download(HttpServletResponse response){

    }
}
