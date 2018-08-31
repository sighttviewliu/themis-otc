package com.oxchains.themis.user.rest;

import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.user.service.SystemInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ccl
 * @time 2018-05-07 14:06
 * @name InfoController
 * @desc:
 */
@RestController
public class InfoController {
    @Resource
    private SystemInfoService systemInfoService;
    @GetMapping(value = "/")
    public RestResp info(){
        return systemInfoService.info();
    }

    @GetMapping(value = "/info/organ")
    public RestResp getOrganization(Long id){
        return systemInfoService.getOrganizationInfo(null == id ? 1L : id);
    }

    @GetMapping(value = "/info/banner")
    public RestResp getBannerImage(){
        return systemInfoService.getBannerTip();
    }

    @GetMapping(value = "/info/tips")
    public RestResp getBannerInfo(){
        return systemInfoService.getNewsTip();
    }
    @GetMapping(value = "/info/prod")
    public RestResp getProductInfo(String code){
        return systemInfoService.getProductInfo(code == null ? "THEMIS" : code);
    }
}
