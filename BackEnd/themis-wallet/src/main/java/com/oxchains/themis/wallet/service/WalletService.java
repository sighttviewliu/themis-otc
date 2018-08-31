package com.oxchains.themis.wallet.service;

import com.oxchains.themis.common.ethereum.Web3jHandler;
import com.oxchains.themis.common.model.PageModel;
import com.oxchains.themis.common.model.RestResp;

import com.oxchains.themis.common.redis.RedisService;
import com.oxchains.themis.repo.dao.wallet.ThemisTokenRepo;
import com.oxchains.themis.repo.dao.wallet.UserWalletRepo;
import com.oxchains.themis.repo.dao.wallet.WalletTransactionRepo;
import com.oxchains.themis.repo.entity.wallet.ThemisToken;
import com.oxchains.themis.repo.entity.wallet.UserWallet;
import com.oxchains.themis.repo.entity.wallet.UserWalletVO;
import com.oxchains.themis.repo.entity.wallet.WalletTransaction;
import com.oxchains.themis.wallet.common.ResParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.web3j.protocol.core.methods.response.Transaction;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ccl
 * @time 2018-06-07 11:01
 * @name WalletService
 * @desc:
 */
@Transactional
@Slf4j
@Service
public class WalletService {

    @Resource
    private UserWalletRepo userWalletRepo;
    @Resource
    private WalletTransactionRepo walletTransactionRepo;
    @Resource
    private ResParam resParam;

    @Resource
    private ThemisTokenRepo themisTokenRepo;

    @Resource
    private RedisService redisService;

    public RestResp getWallets(Long userId, Integer pageSize, Integer pageNo){
        if(null == userId){
            return RestResp.fail();
        }
//        pageNo = pageNo == null ? 1 : pageNo;
//        pageSize = pageSize == null ? 10 : pageSize;
        List<UserWalletVO> result = null;
        Pageable pager = null;
        Page<UserWallet> page = null;
        try{
            if(null != pageNo && null != pageSize){
                pager = new PageRequest(pageNo - 1, pageSize);
                page = userWalletRepo.findByUserId(userId, pager);
                result = toList(page.getContent());
                return RestResp.success("",new PageModel(page.getTotalPages(), page.getTotalElements(), result));
            }else {
                List<UserWallet> list = userWalletRepo.findByUserId(userId);
                result = toList(list);
                return RestResp.success("",new PageModel(null != result ? 1: 0, null != result ? result.size(): 0, result));
            }
        }catch (Exception e){
            log.error("查询出错", e);
        }
        return RestResp.fail();
    }
    public RestResp addAddress(Long userId, Integer type,String address){
        if(null == address){
            return RestResp.fail("Parameter NULL");
        }
        try{
            //UserWallet userWallet = userWalletRepo.findByUserIdAndType(userId, type);
            UserWallet userWallet = userWalletRepo.findByAddress(address);
            if(null == userWallet){
                userWallet = new UserWallet();
            }else {
                return RestResp.fail("address exist");
            }
            userWallet.setUserId(userId);
            userWallet.setType(type);
            userWallet.setAddress(address);
            userWallet.setCreateTime(System.currentTimeMillis());
      /*      String addr = userWallet.getAddress();
            if(null == addr || "".equals(addr.trim())){
                addr = ""+address;
            }else {
                addr = addr + ","+ address;
            }
            userWallet.setAddress(addr);*/
            userWallet = userWalletRepo.save(userWallet);
            return RestResp.success(toWalletVO(userWallet));
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            log.error("添加地址失败", e);
        }
        return RestResp.fail();

    }
    public RestResp sendTransaction( Long userId,String from ,String to, Double amount, String hash){
        WalletTransaction walletTransaction = null;
        if(null == hash){
            return RestResp.fail("Parameter NULL");
        }
        try{
            String result = Web3jHandler.getInstance(ResParam.getEthPoaServerUrl()).sendRawTransactions(hash);
            if(null != result){
                walletTransaction = new WalletTransaction(System.currentTimeMillis(),userId,from,to, amount,result );
                walletTransactionRepo.save(walletTransaction);
                return RestResp.success("", result);
            }
        }catch (Exception e){
            log.error("发送交易失败", e);
            return RestResp.fail(e.getMessage());
        }
        return RestResp.fail();
    }

    public RestResp sendTransaction(String from ,String to, Double amount, String hash){
        if(null == hash){
            return RestResp.fail("Parameter NULL");
        }
        try{
            String result = Web3jHandler.getInstance(ResParam.getEthPoaServerUrl()).sendRawTransactions(hash);
            if(null != result){
                return RestResp.success("", result);
            }
        }catch (Exception e){
            log.error("发送交易失败", e);
            return RestResp.fail(e.getMessage());
        }
        return RestResp.fail();
    }

    public RestResp getTransaction(String txId){
        try{
            return  RestResp.success("",Web3jHandler.getInstance(ResParam.getEthPoaServerUrl()).getTransaction(txId));
        }catch (Exception e){
            log.error("查询交易详情失败", e);
        }
        return RestResp.fail();
    }
    public RestResp getTransactions(Long userId, Integer pageSize, Integer pageNo){
        if (null == userId) {
            return RestResp.fail();
        }
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 10 : pageSize;
        Pageable pager = new PageRequest(pageNo - 1, pageSize);
        try{
            Page<WalletTransaction> page = walletTransactionRepo.findByUserId(userId, pager);
            return RestResp.success("", new PageModel(page.getTotalPages(),page.getTotalElements(), page.getContent()));
        }catch (Exception e){
            log.error("查询交易失败", e);
        }
        return RestResp.fail();
    }
    public RestResp getNonce(String address){
        try{
            return RestResp.success("", Web3jHandler.getInstance(ResParam.getEthPoaServerUrl()).getNonceLatest(address));
        }catch (Exception e){
            log.error("获取NONCE失败", e);
            return RestResp.fail("获取NONCE失败", e.getMessage());
        }
    }
    public RestResp getBalance(String address){
        try{
            return RestResp.success("", Web3jHandler.getInstance(ResParam.getEthPoaServerUrl()).getBalance(address));
        }catch (Exception e){
            log.error("查询失败", e);
            return RestResp.fail("查询失败", e.getMessage());
        }
    }

    public RestResp checkToken(String address){
        if(null == address || !address.startsWith("0x") || !(address.length() == 42)){
            return RestResp.fail("Address Error");
        }
        try{
            ThemisToken themisToken = themisTokenRepo.findByAddress(address);
            if(null != themisToken){
                return RestResp.fail("Already Got: ", themisToken);
            }
            return RestResp.success();
        }catch (Exception e){
            log.error("查询失败" ,e);
        }
        return RestResp.fail("查询失败");

    }
    public RestResp getToken(String address){
        if(null == address || !address.startsWith("0x") || !(address.length() == 42)){
            return RestResp.fail("Address Error");
        }
        try{
            ThemisToken themisToken = themisTokenRepo.findByAddress(address);
            if(null != themisToken){
                return RestResp.fail("Already Got");
            }
            if(redisService.setNx(address,address)){
                themisToken = new ThemisToken();
                themisToken.setCreateTime(System.currentTimeMillis());
                themisToken.setAddress(address);
                themisToken.setAmount(ResParam.getThemisTokenAdminAmount());
                themisToken = themisTokenRepo.save(themisToken);
                String txhash = Web3jHandler.getInstance(ResParam.getEthPoaServerUrl()).transfer(ResParam.getThemisTokenAdminSource(), ResParam.getThemisTokenAdminPassword(), address, new BigDecimal(ResParam.getThemisTokenAdminAmount()));
                themisToken.setTxhash(txhash);
                try{
                    themisToken = themisTokenRepo.save(themisToken);
                }catch (Exception e){
                    log.error("sync themis token error");
                }
                return RestResp.success("获取TOKEN成功", themisToken);
            }
            return RestResp.fail("获取TOKEN失败");
        }catch (Exception e){
            log.error("获取TOKEN失败 ", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return RestResp.fail("获取TOKEN失败", e.getMessage());
        }finally {
            redisService.remove(address);
        }
    }

    private UserWalletVO toWalletVO(UserWallet userWallet) throws IOException {
        if(null == userWallet){
            return null;
        }
        UserWalletVO vo = new UserWalletVO(userWallet);
        String address = userWallet.getAddress();
        if(null != address && !"".equals(address.trim())){
            String[] addrs = address.split(",");
            BigDecimal balance = new BigDecimal(0);
            for(String addr : addrs){
                BigDecimal bal = Web3jHandler.getInstance(ResParam.getEthPoaServerUrl()).getBalance(addr);
                balance = balance.add(bal);
            }
            vo.setBalance(balance.doubleValue());
        }
        vo.setWalletName("GET");
        return vo;
    }
    private List<UserWalletVO>  toList(List<UserWallet> list) throws IOException {
        List<UserWalletVO> result = null;
        if(null != list && list.size() > 0){
            result = new ArrayList<>(list.size());
            for(UserWallet userWallet: list){
                result.add(toWalletVO(userWallet));
            }
        }
        return result;
    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void transactionListener() {
        List<WalletTransaction> saveList = null;
        try {
            List<WalletTransaction> transactions = walletTransactionRepo.findByFromAddrIsNullAndHashIsNotNull();
            if(null != transactions && transactions.size() > 0){
                saveList = new ArrayList<>(transactions.size());
                for(WalletTransaction walletTransaction : transactions){
                    Transaction transaction = Web3jHandler.getInstance(ResParam.getEthPoaServerUrl()).getTransaction(walletTransaction.getHash());
                    if(null != transaction){
                        walletTransaction.setFromAddr(transaction.getFrom());
                        walletTransaction.setBlockNumber(transaction.getBlockNumber().longValue());
                        saveList.add(walletTransaction);
                    }
                }
                walletTransactionRepo.save(saveList);
            }
        } catch (IOException e) {
            log.error("监听交易失败", e);
        }

    }

}
