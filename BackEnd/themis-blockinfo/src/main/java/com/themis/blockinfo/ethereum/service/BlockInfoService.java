package com.themis.blockinfo.ethereum.service;

import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.model.RestRespPage;
import com.themis.blockinfo.ethereum.entity.BlockInfo;
import com.themis.blockinfo.ethereum.repo.BlockInfoRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ccl
 * @time 2018-05-22 16:46
 * @name BlockInfoService
 * @desc:
 */
@Service
public class BlockInfoService {
    @Resource
    private BlockInfoRepo blockInfoRepo;

    public RestResp getBlockInfo(Integer pageNo, Integer pageSize){
        pageNo = pageNo==null?1:pageNo;
        pageSize = pageSize==null?10:pageSize;
        Pageable pageable = new PageRequest(pageNo-1, pageSize, new Sort(Sort.Direction.DESC,"number"));
        try{
            Page<BlockInfo> page = blockInfoRepo.findAll(pageable);
            return RestRespPage.success(page.getContent(),page.getTotalPages());
        }catch (Exception e){
            e.printStackTrace();
        }
        return RestResp.fail();
    }
}
