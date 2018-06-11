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
    public RestResp getWallets(@PathVariable Long userId){
        return walletService.getWallets(userId);
    }
    @PostMapping(value = "/address/{userId}/{type}")
    public RestResp addAddress(@PathVariable Long userId, @PathVariable Integer type, @RequestBody ReqParam param){
        return walletService.addAddress(userId, type, param.getAddress());
    }
    @PostMapping(value = "/transaction")
    public RestResp sendTransaction(@RequestBody ReqParam param){
        return walletService.sendTransaction(param.getHash());
    }

    @GetMapping(value = "/transaction/{txId}")
    public RestResp getTransaction(@PathVariable String txId){
        return walletService.getTransaction(txId);
    }

    @GetMapping(value = "/nonce/{address}")
    public RestResp getAddressNonce(@PathVariable String address){
        return walletService.getNonce(address);
    }
}
