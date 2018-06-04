package com.oxchains.themis.blockinfo.ethereum.service;

import com.oxchains.themis.common.ethereum.Web3jHandler;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.model.RestRespPage;
import com.oxchains.themis.blockinfo.ethereum.entity.EthBlockInfo;
import com.oxchains.themis.blockinfo.ethereum.repo.EthBlockInfoRepo;
import com.oxchains.themis.common.util.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author ccl
 * @time 2018-05-22 16:46
 * @name BlockInfoService
 * @desc:
 */
@Slf4j
@Service
public class BlockInfoService {

    @Value("${eth.server.url}")
    private String ethServerUrl;
    @Resource
    private EthBlockInfoRepo blockInfoRepo;

    public RestResp getEthBlockInfo(Integer pageNo, Integer pageSize){
        pageNo = pageNo==null?1:pageNo;
        pageSize = pageSize==null?10:pageSize;
        Pageable pageable = new PageRequest(pageNo-1, pageSize, new Sort(Sort.Direction.DESC,"number"));
        try{
            Page<EthBlockInfo> page = blockInfoRepo.findAll(pageable);
            return RestRespPage.success(page.getContent(),page.getTotalPages());
        }catch (Exception e){
            e.printStackTrace();
        }
        return RestResp.fail();
    }

    public RestResp getEthBlockInfo(String block){
        if(null == block){
            return RestResp.fail("请正确填写区块查询信息");
        }
        EthBlockInfo blockInfo = null;
        try{
            if(block.startsWith("0x") && block.length() == 66){
                blockInfo = blockInfoRepo.findByHash(block);
                //blockInfo = EthBlockInfo.block2BlockInfo(Web3jHandler.getInstance(ethServerUrl).getBlock(block));
            }else if(RegexUtils.match(block,RegexUtils.REGEX_POSITIVE_INTEGER)){
                blockInfo = blockInfoRepo.findByNumber(new BigInteger(block));
                //blockInfo = EthBlockInfo.block2BlockInfo(Web3jHandler.getInstance(ethServerUrl).getBlock(new BigInteger(block)));
            }else {

            }
            return RestResp.success(blockInfo);
        }catch (Exception e){
            log.error("查询区块信息出错",e);
        }
        return RestResp.fail();
    }
    public RestResp getEthBalance(String address){
        try {
            BigDecimal balance = Web3jHandler.getInstance(ethServerUrl).getBalance(address);
            return RestResp.success(balance);
        } catch (IOException e) {
            log.error("查询地址余额失败", e);
            return RestResp.fail();
        }
    }

    public RestResp search(String block){
        if(null == block){
            return RestResp.fail("请正确填写区块查询信息");
        }
        try{
            if(block.startsWith("0x") && block.length() == 66){
                EthBlockInfo blockInfo = EthBlockInfo.block2BlockInfo(Web3jHandler.getInstance(ethServerUrl).getBlock(block));
                return RestResp.success(blockInfo);
            }else if(RegexUtils.match(block,RegexUtils.REGEX_POSITIVE_INTEGER)){
                EthBlockInfo blockInfo = EthBlockInfo.block2BlockInfo(Web3jHandler.getInstance(ethServerUrl).getBlock(new BigInteger(block)));
                return RestResp.success(blockInfo);
            }else if(block.startsWith("0x") && block.length()==42){
                return getEthBalance(block);
            }else {
            }
        }catch (Exception e){
            log.error("查询区块信息出错",e);
        }
        return RestResp.fail("输入的查询信息有误");
    }
}
