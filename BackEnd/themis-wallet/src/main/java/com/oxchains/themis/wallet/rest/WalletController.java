package com.oxchains.themis.wallet.rest;

import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.wallet.common.ReqParam;
import com.oxchains.themis.wallet.service.WalletService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ccl
 * @time 2018-06-07 10:57
 * @name WalletController
 * @desc:
 */
@RequestMapping(value = "/wallet")
@RestController
public class WalletController {

    @Resource
    private WalletService walletService;

    @GetMapping(value = "/list/{userId}")
    public RestResp getWallets(@PathVariable Long userId, Integer pageSize, Integer pageNo){
        return walletService.getWallets(userId, pageSize, pageNo);
    }
    @PostMapping(value = "/address/{userId}/{type}")
    public RestResp addAddress(@PathVariable Long userId, @PathVariable Integer type, @RequestBody ReqParam param){
        return walletService.addAddress(userId, type, param.getAddress());
    }
    @PostMapping(value = "/transaction/{userId}")
    public RestResp sendTransaction(@PathVariable Long userId, @RequestBody ReqParam param){
        return walletService.sendTransaction(userId, param.getFrom(), param.getTo(),param.getAmount(), param.getHash());
    }

    @PostMapping(value = "/transaction")
    public RestResp sendTransaction(@RequestBody ReqParam param){
        return walletService.sendTransaction(param.getFrom(), param.getTo(),param.getAmount(), param.getHash());
    }

    @GetMapping(value = "/transaction/{txId}")
    public RestResp getTransaction(@PathVariable String txId){
        return walletService.getTransaction(txId);
    }

    @GetMapping(value = "/transactions/{userId}/{pageSize}/{pageNo}")
    public RestResp getTransactions(@PathVariable Long userId, @PathVariable Integer pageSize, @PathVariable Integer pageNo){
        return walletService.getTransactions(userId, pageSize, pageNo);
    }

    @GetMapping(value = "/nonce/{address}")
    public RestResp getAddressNonce(@PathVariable String address){
        return walletService.getNonce(address);
    }

    @GetMapping(value = "/balance/{address}")
    public RestResp getBalance(@PathVariable String address){
        return walletService.getBalance(address);
    }

    @PostMapping(value = "/etoken")
    public RestResp getToken(@RequestBody ReqParam param){
        return walletService.getToken(param.getAddress());
    }
    @GetMapping(value = "/etoken/{address}")
    public RestResp checkToken(@PathVariable String address){
        return walletService.checkToken(address);
    }
}
