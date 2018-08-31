package com.oxchains.themis.blockinfo.ethereum.rest;

import com.oxchains.themis.blockinfo.ethereum.service.BlockInfoService;
import com.oxchains.themis.common.model.RestResp;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;

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

    @GetMapping(value = "/block/search/{block}")
    public RestResp search(@PathVariable String block) {
        return blockInfoService.search(block);
    }

    @GetMapping(value = "/blockinfo/{pageNo}/{pageSize}")
    public RestResp getBlockInfo(@PathVariable Integer pageNo, @PathVariable Integer pageSize) {
        return blockInfoService.getEthBlockInfo(pageNo, pageSize);
    }

    @GetMapping(value = "/block/{block}")
    public RestResp getBlockInfo(@PathVariable String block) {
        return blockInfoService.getEthBlockInfo(block);
    }

    @GetMapping(value = "/address/{address}")
    public RestResp getBalance(@PathVariable String address) {
        return blockInfoService.getEthBalance(address);
    }

    @GetMapping(value = "/transaction/detail/{txhash}")
    public RestResp getTransaction(@PathVariable String txhash) {
        return blockInfoService.getTransaction(txhash);
    }

    @GetMapping(value = "/transactions/{pageSize}/{pageNo}")
    public RestResp getTransactionRecord(@PathVariable Integer pageSize, @PathVariable Integer pageNo) {
        return blockInfoService.getTransactionRecord(pageSize, pageNo);
    }
    @GetMapping(value = "/transactions/blockhash/{blockhash}/{pageSize}/{pageNo}")
    public RestResp getTransactionByBlockHash(@PathVariable String blockhash, @PathVariable Integer pageSize, @PathVariable Integer pageNo) {
        return blockInfoService.getTransactionByBlock(blockhash, pageSize, pageNo);
    }

    @GetMapping(value = "/transactions/blocknum/{blocknum}/{pageSize}/{pageNo}")
    public RestResp getTransactionByBlockNumber(@PathVariable BigInteger blocknum, @PathVariable Integer pageSize, @PathVariable Integer pageNo) {
        return blockInfoService.getTransactionByBlock(blocknum, pageSize, pageNo);
    }

    @GetMapping(value = "/transactions/address/{address}/{pageSize}/{pageNo}")
    public RestResp getTransactionRecord(@PathVariable String address, @PathVariable Integer pageSize, @PathVariable Integer pageNo) {
        return blockInfoService.getTransactionRecord(address, pageSize, pageNo);
    }

    @PostMapping(value = "/check/sync/block/{startNum}/{endNum}")
    public RestResp checkSyncBlock(@PathVariable BigInteger startNum, @PathVariable BigInteger endNum){
        return blockInfoService.checkSyncBlock(startNum, endNum);
    }
    @PostMapping(value = "/check/sync/transaction/{startNum}/{endNum}")
    public RestResp checkSyncTransaction(@PathVariable BigInteger startNum, @PathVariable BigInteger endNum){
        return blockInfoService.checkSyncTransaction(startNum, endNum);
    }

    @PostMapping(value = "/sync/block/{startNum}/{endNum}")
    public RestResp syncBlock(@PathVariable BigInteger startNum, @PathVariable BigInteger endNum){
        return blockInfoService.syncBlock(startNum, endNum);
    }

}
