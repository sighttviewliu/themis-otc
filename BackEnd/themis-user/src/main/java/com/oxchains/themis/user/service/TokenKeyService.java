package com.oxchains.themis.user.service;

import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.repo.dao.TokenKeyDao;
import com.oxchains.themis.repo.entity.TokenKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ccl
 * @time 2018-09-07 11:51
 * @name TokenKeyService
 * @desc:
 */
@Slf4j
@Service
public class TokenKeyService {
    @Resource
    private TokenKeyDao tokenKeyDao;

    public RestResp token(Long id){
        try{
            TokenKey tokenKey = tokenKeyDao.findTokenKeyById(id == null ? 1L : id);
            return RestResp.success(tokenKey);
        }catch (Exception e){
            log.error("Get token key error ---> ", e);
        }
        return RestResp.fail();
    }

    public RestResp token(TokenKey tokenKey){
        if(null == tokenKey || null == tokenKey.getPriKey() || null == tokenKey.getPubKey()){
            return RestResp.fail("Token key is null");
        }
        try{
            tokenKey = tokenKeyDao.save(tokenKey);
            return RestResp.success(tokenKey);
        }catch (Exception e){
            log.error("save token key error ---> ", e);
        }
        return RestResp.fail();
    }

}
