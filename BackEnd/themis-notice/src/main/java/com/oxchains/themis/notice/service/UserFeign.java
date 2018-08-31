package com.oxchains.themis.notice.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ccl
 * @time 2018-07-07 11:26
 * @name UserFeign
 * @desc:
 */
@FeignClient(name = "themis-user")
public interface UserFeign {

    /**
     * 检查实名认证
     */
    @GetMapping(value = "/user/check/qic/{userId}")
    JSONObject checkQIC(@PathVariable("userId") Long userId);

    @GetMapping(value = "/user/findOne")
    String findUserById(@RequestParam("id") Long id);
}