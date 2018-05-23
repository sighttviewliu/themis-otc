package com.themis.blockinfo.ethereum.rest;

import com.oxchains.themis.common.model.RestResp;
import com.themis.blockinfo.ethereum.service.BlockInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ccl
 * @time 2018-05-22 17:00
 * @name EthController
 * @desc:
 */
@RestController
@RequestMapping(value = "/eth")
public class EthController {

    @Resource
    private BlockInfoService blockInfoService;

    @GetMapping(value = "/blockinfo/{pageNo}/{pageSize}")
    public RestResp blockInfo(@PathVariable Integer pageNo, @PathVariable Integer pageSize){
        return blockInfoService.getBlockInfo(pageNo,pageSize);
    }

}
