package com.oxchains.themis.notice.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author ccl
 * @time 2018-07-09 15:29
 * @name OrderFeign
 * @desc:
 */

@FeignClient(name = "themis-order")
public interface OrderFeign {
    @GetMapping(value = "/order/deal/history/summary/{userId}/{coinType}")
    JSONObject getDealHistory(@PathVariable("userId") Long userId,@PathVariable("coinType") Long coinType);
}
