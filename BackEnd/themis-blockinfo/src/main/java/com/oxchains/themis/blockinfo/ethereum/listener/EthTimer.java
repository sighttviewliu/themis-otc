package com.oxchains.themis.blockinfo.ethereum.listener;

import com.oxchains.themis.blockinfo.ethereum.entity.EthBlockInfo;
import com.oxchains.themis.blockinfo.ethereum.entity.EthTransactionInfo;
import com.oxchains.themis.blockinfo.ethereum.entity.ResParam;
import com.oxchains.themis.blockinfo.ethereum.repo.EthBlockInfoRepo;
import com.oxchains.themis.blockinfo.ethereum.repo.EthTransactionInfoRepo;
import com.oxchains.themis.common.constant.Constants;
import com.oxchains.themis.common.ethereum.Web3jHandler;
import com.oxchains.themis.common.redis.RedisService;
import com.oxchains.themis.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import rx.Subscription;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author ccl
 * @time 2018-06-25 11:45
 * @name EthTimer
 * @desc:
 */
@Component
@Slf4j
public class EthTimer {
    //private static String serverUrl = "http://101.254.183.20:8556";
    private  static Web3j web3j = null;
    private Subscription subscription = null;

    @Resource
    private RedisService redisService;

    @Resource
    EthBlockInfoRepo ethBlockInfoRepo;

    @Resource
    private EthTransactionInfoRepo ethTransactionInfoRepo;

    private static final ConcurrentSkipListMap<String, String> blockInfoTreeMap = new ConcurrentSkipListMap<String, String>();

    @Scheduled(cron = "0/30 * * * * ?")
    public void subscribe(){
        try {
            if(null == web3j || subscription == null){
                log.info("SERVER_URL: {}", ResParam.getEthBlockinfoServer());
                web3j = Web3j.build(new HttpService(ResParam.getEthBlockinfoServer()));
                subscription = web3j.blockObservable(false).subscribe(ethBlock -> {
                    EthBlock.Block block = ethBlock.getBlock();
                    EthBlockInfo blockInfo = EthBlockInfo.block2BlockInfo(block);
                    log.info("{}",blockInfo);
                    //ethBlockInfoRepo.save(blockInfo);
                    BigInteger latestNum = block.getNumber();
                    blockInfoTreeMap.put(latestNum.toString(), JsonUtil.toJson(blockInfo));
                    clearMap(latestNum, 10);
                    saveMapToRedis(latestNum);
                    saveBlock(blockInfo);
                    saveTransaction(block);
                });
            }
        }catch (Exception e){
            web3j = null;
            subscription = null;
            log.error("ETH Lister Error :", e);
        }
    }

    private void clearMap(BigInteger latestNum, int remain){
        try{
            if(blockInfoTreeMap.size() > remain){
                log.info("clearMap(): {}", latestNum);
                BigInteger limit = latestNum.subtract(new BigInteger("" + remain));
                Iterator iterator = blockInfoTreeMap.keySet().iterator();
                while (iterator.hasNext()){
                    String key = (String) iterator.next();
                    BigInteger b = new BigInteger(key);
                    if(b.compareTo(limit)<=0){
                        blockInfoTreeMap.remove(key.toString());
                    }
                }
            }
        }catch (Exception e){
            log.error("clear map error: ", e);
        }
    }

    private void saveMapToRedis(BigInteger latestNum){
        try{
            log.info("saveMapToRedis(): {}",  latestNum);
            Map map = blockInfoTreeMap.descendingMap();
            redisService.remove(Constants.POA_LATEST_BLOCK_10);
            redisService.addMap(Constants.POA_LATEST_BLOCK_10, map);
            redisService.set(Constants.POA_LATEST_BLOCK_NUM, latestNum);
        }catch (Exception e){
            log.error("save map to redis error: ", e);
        }
    }

    private void saveBlock(EthBlockInfo ethBlockInfo){
        try{
            log.info("saveBlock(): {}", ethBlockInfo.getNumber());
            ethBlockInfoRepo.save(ethBlockInfo);
        }catch (Exception e){
            log.error("save block to database error: ", e);
        }
    }

    private void saveTransaction(EthBlock.Block block){
        log.info("saveTransaction(): {}", block.getNumber());
        List<EthBlock.TransactionResult> list = block.getTransactions();
        List<EthTransactionInfo> infos = null;
        if(null != list && list.size()>0){
            infos = new ArrayList<>(list.size());
            EthTransactionInfo info = null;
            for(EthBlock.TransactionResult result : list){
                String hash = (String) result.get();
                Transaction transaction = null;
                try {
                    transaction = web3j.ethGetTransactionByHash(hash).send().getResult();//Web3jHandler.getInstance(ResParam.getEthBlockinfoServer()).getTransaction(hash);
                } catch (IOException e) {
                    log.error("Get Transaction by Hash Error: ", e);
                }
                if(null != transaction){
                    log.info("-----------------value: " + transaction.getValue());
                    info = new EthTransactionInfo(transaction);
                    info.setTimestamp(block.getTimestamp() == null ? null : block.getTimestamp().longValue());
                    try {
                        TransactionReceipt receipt = web3j.ethGetTransactionReceipt(transaction.getHash()).send().getResult();
                        BigInteger gasUsed = receipt.getGasUsed();
                        String txstatus = receipt.getStatus();
                        info.setGasUsed(gasUsed == null ? null : gasUsed.toString());
                        info.setTxstatus(txstatus);
                    } catch (IOException e) {
                        log.error("Get Gas Used Error", e);
                    }
                    infos.add(info);
                }
            }
            if(null != infos && infos.size()>0){
                ethTransactionInfoRepo.save(infos);
            }
        }
    }

    public void getLatestBlock(){
        BigInteger num = (BigInteger) redisService.get(Constants.POA_LATEST_BLOCK_NUM);
        log.info("Get Latest block Timer run: {}", num);
        try {
            web3j = Web3j.build(new HttpService(ResParam.getEthBlockinfoServer()));
            EthBlock.Block block = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock();
            if (null == num || num.compareTo(block.getNumber()) < 0) {
                num = block.getNumber();
                EthBlockInfo blockInfo = EthBlockInfo.block2BlockInfo(block);
                ethBlockInfoRepo.save(blockInfo);
                redisService.set(Constants.POA_LATEST_BLOCK_NUM, num);
            }
        } catch (IOException e) {
            log.error("Get POA Latest Block Num IO Error: ", e);
        }

    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkBlockInfo(){
        try {
            log.info("Check Info SERVER_URL: {}", ResParam.getEthBlockinfoServer());
            web3j = Web3j.build(new HttpService(ResParam.getEthBlockinfoServer()));
            BigInteger latestNum = Web3jHandler.getInstance(ResParam.getEthBlockinfoServer()).getLatestBlockNumber();
            int size = ResParam.getEthBlockCheckStep();
            log.info("Check Size = ", size);
            BigInteger startNum = latestNum.subtract(new BigInteger(size + ""));
            if(startNum.longValue() < 0L){
                startNum = new BigInteger("0");
            }
            List<BigInteger> list = ethBlockInfoRepo.findEthBlockNumber(startNum, latestNum);
            if (null != list && list.size() == size) {
                return;
            }
            for (; startNum.compareTo(latestNum) <= 0; startNum = startNum.add(new BigInteger("1"))) {
                if (!list.contains(startNum)) {
                    EthBlock.Block block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(startNum), false).send().getBlock();
                    EthBlockInfo blockInfo = EthBlockInfo.block2BlockInfo(block);
                    ethBlockInfoRepo.save(blockInfo);
                    log.info("save block: ------------" + block.getNumber());
                    saveTransaction(block);
                }
            }
        }catch (Exception e){
            log.error("ETH Lister Error :", e);
        }
    }
}
