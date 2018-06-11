package com.oxchains.themis.wallet.service;

import com.oxchains.themis.common.ethereum.Web3jHandler;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.repo.dao.user.UserWalletRepo;
import com.oxchains.themis.repo.entity.user.UserWallet;
import com.oxchains.themis.repo.entity.user.UserWalletVO;
import com.oxchains.themis.wallet.common.ResParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ccl
 * @time 2018-06-07 11:01
 * @name WalletService
 * @desc:
 */
@Service
public class WalletService {

    @Resource
    private UserWalletRepo userWalletRepo;
    @Resource
    private ResParam resParam;

    public RestResp getWallets(Long userId){
        if(null == userId){
            return RestResp.fail();
        }
        try{
            List<UserWallet> list = userWalletRepo.findByUserId(userId);
            List<UserWalletVO> result = toList(list);
            return RestResp.success("",result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return RestResp.fail();
    }
    public RestResp addAddress(Long userId, Integer type,String address){
        if(null == address){
            return RestResp.fail("Parameter NULL");
        }
        try{
            UserWallet userWallet = userWalletRepo.findByUserIdAndType(userId, type);
            if(null == userWallet){
                userWallet = new UserWallet();
            }
            userWallet.setUserId(userId);
            userWallet.setType(type);
            String addr = userWallet.getAddress();
            if(null == addr || "".equals(addr.trim())){
                addr = ""+address;
            }else {
                addr = addr + ","+ address;
            }
            userWallet.setAddress(addr);
            userWallet = userWalletRepo.save(userWallet);
            return RestResp.success(toWalletVO(userWallet));
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        }
        return RestResp.fail();

    }
    public RestResp sendTransaction(String hash){
        if(null == hash){
            return RestResp.fail("Parameter NULL");
        }
        try{
            String result = Web3jHandler.getInstance(ResParam.getEthPoaServerUrl()).sendRawTransactions(hash);
            if(null != result){
                return RestResp.success("", result);
            }
        }catch (Exception e){
            e.printStackTrace();
            return RestResp.fail(e.getMessage());
        }
        return RestResp.fail();
    }

    public RestResp getTransaction(String txId){
        try{
            return  RestResp.success("",Web3jHandler.getInstance(ResParam.getEthPoaServerUrl()).getTransaction(txId));
        }catch (Exception e){
            e.printStackTrace();
        }
        return RestResp.fail();
    }
    public RestResp getNonce(String address){
        try{
            return RestResp.success("", Web3jHandler.getInstance(ResParam.getEthPoaServerUrl()).getPendingNonce(address));
        }catch (Exception e){
            return RestResp.fail("", e.getMessage());
        }
    }
    private UserWalletVO toWalletVO(UserWallet userWallet) throws IOException {
        if(null == userWallet){
            return null;
        }
        UserWalletVO vo = new UserWalletVO(userWallet);
        String address = userWallet.getAddress();
        if(null != address && "".equals(address.trim())){
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
}
