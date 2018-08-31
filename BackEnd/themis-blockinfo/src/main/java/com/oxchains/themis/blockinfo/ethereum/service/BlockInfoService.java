package com.oxchains.themis.blockinfo.ethereum.service;

import com.oxchains.themis.blockinfo.ethereum.entity.*;
import com.oxchains.themis.blockinfo.ethereum.repo.EthTransactionInfoRepo;
import com.oxchains.themis.common.constant.Constants;
import com.oxchains.themis.common.ethereum.Web3jHandler;
import com.oxchains.themis.common.model.PageModel;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.blockinfo.ethereum.repo.EthBlockInfoRepo;
import com.oxchains.themis.common.redis.RedisService;
import com.oxchains.themis.common.util.ArithmeticUtils;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.common.util.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @author ccl
 * @time 2018-05-22 16:46
 * @name BlockInfoService
 * @desc:
 */
@Slf4j
@Service
public class BlockInfoService {

    @Value("${eth.blockinfo.server}")
    private String ethServerUrl;
    @Resource
    private EthBlockInfoRepo blockInfoRepo;

    @Resource
    private EthTransactionInfoRepo ethTransactionInfoRepo;

    public RestResp getEthBlockInfo(Integer pageNo, Integer pageSize){
        pageNo = pageNo==null?1:pageNo;
        pageSize = pageSize==null?10:pageSize;

        BigInteger diff = null;
        BigInteger lastNum = null;
        BigInteger lsn = null;
        try {
            lastNum = Web3jHandler.getInstance(ResParam.getEthBlockinfoServer()).getLatestBlockNumber();
        } catch (IOException e) {
            log.error("Get lastNum Error: ",e);
            lastNum = new BigInteger("-1");
        }
        try{
            lsn = (BigInteger) redisService.get(Constants.POA_LATEST_BLOCK_NUM);
        }catch (Exception e){
            log.error("Get lastNum from redis Error: ",e);
        }
        diff = ArithmeticUtils.minus(lastNum, lsn);
        try{
            List<EthBlockInfo> result = null;
            if(null == diff || diff.longValue()>=10){
                result = getBlockInfo(pageSize);
            }else{
                // diff < 10
                result = getBlockInfoFromRedis();
            }
            if(null == result || result.size()<=0 || null == result.get(0).getRealMiner()){
                log.info("*** get block info from database ***");
                Pageable pageable = new PageRequest(pageNo-1, pageSize, new Sort(Sort.Direction.DESC,"number"));
//                Page<EthBlockInfo> page = blockInfoRepo.findAll(pageable);
//                result = page.getContent();
                result = blockInfoRepo.findEthBlockInfosByPage(0, 10);
            }
            return RestResp.success("", listToVOS(result));
        }catch (Exception e){
            log.error("get block list error", e);
        }
        return RestResp.fail();
    }

    public RestResp getEthBlockInfo(String block){
        if(null == block){
            return RestResp.fail("请正确填写区块查询信息");
        }
        EthBlockInfoVO blockInfo = null;
        try{
            if(block.startsWith("0x") && block.length() == 66){
                blockInfo = EthBlockInfoVO.do2VO(Web3jHandler.getInstance(ethServerUrl).getBlock(block));
            }else if("0".equals(block) || RegexUtils.match(block,RegexUtils.REGEX_POSITIVE_INTEGER)){
                blockInfo = EthBlockInfoVO.do2VO(Web3jHandler.getInstance(ethServerUrl).getBlock(new BigInteger(block)));
                if(null == blockInfo){
                    EthBlockInfo block1 = blockInfoRepo.findByNumber(new BigInteger(block));
                    blockInfo = new EthBlockInfoVO(block1);
                }
            }else {

            }
            return RestResp.success("查询区块信息成功", blockInfo);
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

    public RestResp getTransaction(String txhash){
        try {
           EthTransactionInfo info = ethTransactionInfoRepo.findOne(txhash);
           if(null == info){
               info = getTransactionInfo(txhash);
           }
            return RestResp.success("查询交易成功", new EthTransactionInfoVO(info));
        } catch (Exception e) {
            log.error("查询交易失败", e);
            return RestResp.fail();
        }
    }

    public RestResp getTransactionRecord(Integer pageSize, Integer pageNo){
        pageNo = pageNo==null?1:pageNo;
        pageSize = pageSize==null?10:pageSize;
        Pageable pageable = new PageRequest(pageNo-1, pageSize, new Sort(Sort.Direction.DESC,"blockNumber"));
        try {
            if(pageSize == 3){
                List<EthTransactionInfo> list = ethTransactionInfoRepo.findEthTransactionInfosByPage(0, 3);
                return RestResp.success("查询交易成功", new PageModel(0, 0L, list2VOS(list)));
            }
            Page<EthTransactionInfo> page = ethTransactionInfoRepo.findAll(pageable);
            return RestResp.success("查询交易成功", new PageModel(page.getTotalPages(), page.getTotalElements(), list2VOS(page.getContent())));
        } catch (Exception e) {
            log.error("查询交易失败", e);
            return RestResp.fail();
        }
    }

    public RestResp getTransactionByBlock(String blockHash,Integer pageSize, Integer pageNo){
        pageNo = pageNo==null?1:pageNo;
        pageSize = pageSize==null?10:pageSize;
        Pageable pageable = new PageRequest(pageNo-1, pageSize, new Sort(Sort.Direction.DESC,"blockNumber"));
        try {
            Page<EthTransactionInfo> page = ethTransactionInfoRepo.findByBlockHash(blockHash, pageable);
            return RestResp.success("查询交易成功", new PageModel(page.getTotalPages(), page.getTotalElements(), list2VOS(page.getContent())));
        } catch (Exception e) {
            log.error("查询交易失败", e);
            return RestResp.fail();
        }
    }

    public RestResp getTransactionByBlock(BigInteger blockNumber,Integer pageSize, Integer pageNo){
        pageNo = pageNo==null?1:pageNo;
        pageSize = pageSize==null?10:pageSize;
        Pageable pageable = new PageRequest(pageNo-1, pageSize, new Sort(Sort.Direction.DESC,"blockNumber"));
        try {
            Page<EthTransactionInfo> page = ethTransactionInfoRepo.findByBlockNumber(blockNumber, pageable);
            return RestResp.success("查询交易成功", new PageModel(page.getTotalPages(), page.getTotalElements(), list2VOS(page.getContent())));
        } catch (Exception e) {
            log.error("查询交易失败", e);
            return RestResp.fail();
        }
    }

    public RestResp getTransactionRecord(String address, Integer pageSize, Integer pageNo){
        pageNo = pageNo==null?1:pageNo;
        pageSize = pageSize==null?10:pageSize;
        Pageable pageable = new PageRequest(pageNo-1, pageSize, new Sort(Sort.Direction.DESC,"blockNumber"));
        try {
            Page<EthTransactionInfo> page = ethTransactionInfoRepo.findByTransactionRecordByAddress(address, pageable);
            return RestResp.success("查询交易成功", new PageModel(page.getTotalPages(), page.getTotalElements(), list2VOS(page.getContent())));
        } catch (Exception e) {
            log.error("查询交易失败", e);
            return RestResp.fail();
        }
    }

    public RestResp search(String block){
        if(null == block){
            return RestResp.fail("请正确填写区块查询信息");
        }
        try{
            if(block.startsWith("0x") && block.length() == 66){ //txhash
                //EthBlockInfo blockInfo = EthBlockInfoVO.do2VO(Web3jHandler.getInstance(ethServerUrl).getBlock(block));
                Transaction transaction = Web3jHandler.getInstance(ethServerUrl).getTransaction(block);
                return RestResp.success(transaction);
            }else if(RegexUtils.match(block,RegexUtils.REGEX_POSITIVE_INTEGER)){
                EthBlockInfo blockInfo = EthBlockInfoVO.do2VO(Web3jHandler.getInstance(ethServerUrl).getBlock(new BigInteger(block)));
                return RestResp.success(blockInfo);
            }else if(block.startsWith("0x") && block.length()==42){ //address
                return getEthBalance(block);
            }else {
            }
        }catch (Exception e){
            log.error("查询区块信息出错",e);
        }
        return RestResp.fail("输入的查询信息有误");
    }

    public RestResp checkSyncTransaction(BigInteger startNum, BigInteger endNum){
        if(null == startNum || endNum ==null){
            try {
                endNum = Web3jHandler.getInstance(ethServerUrl).getLatestBlockNumber();
                startNum = endNum.subtract(new BigInteger(ResParam.getEthBlockCheckStep()+ ""));
            } catch (IOException e) {
                log.error("Get latest block number Error 1");
                return RestResp.fail("Check Block Number Error");
            }
        }
        if(startNum.compareTo(endNum) == 1){
            return RestResp.fail("start num grater than end num");
        }
        if(endNum.subtract(startNum).longValue() > 200){
            return RestResp.fail("endNum - startNum is greater than 200");
        }
        try{
            List<EthBlockInfo> blockInfos = blockInfoRepo.findEthBlockInfoByNumber(startNum, endNum);
            if(null == blockInfos || blockInfos.size() <= 0){
                return RestResp.success("Sync complete");
            }
            List<BigInteger> numbers = new ArrayList<>(blockInfos.size());
            blockInfos.stream().forEach(block->{
                numbers.add(block.getNumber());
            });
            List<EthTransactionInfo> transactionInfos = ethTransactionInfoRepo.findByBlockNumberIn(numbers);
            List<BigInteger> result = new ArrayList<>();
            for(EthBlockInfo blockInfo : blockInfos){
                int count = 0;
                int txs = blockInfo.getTxs() == null ? 0: blockInfo.getTxs();
                if(txs == 0){
                    continue;
                }
                for(EthTransactionInfo ethTransactionInfo : transactionInfos){
                    count++;
                }
                if(txs != count){
                    result.add(blockInfo.getNumber());
                }
            }
            if(result.size()> 0){
                return RestResp.success("Some blocks' transaction not sync: ", result);
            }
            return RestResp.success("Transaction Sync complete");
        }catch (Exception e){
            log.error("Check Transaction Error: ", e);
            return RestResp.fail("Check Transaction Error");
        }

    }
    public RestResp checkSyncBlock(BigInteger startNum, BigInteger endNum){
        if(null == startNum || endNum ==null){
            try {
                endNum = Web3jHandler.getInstance(ethServerUrl).getLatestBlockNumber();
                startNum = endNum.subtract(new BigInteger(ResParam.getEthBlockCheckStep()+ ""));
            } catch (IOException e) {
                log.error("Get latest block number Error 1");
                return RestResp.fail("Check Block Number Error");
            }
        }
        if(startNum.compareTo(endNum) == 1){
            return RestResp.fail("start num grater than end num");
        }
        List<BigInteger> list = blockInfoRepo.findEthBlockNumber(startNum, endNum);
        if(null == list || list.size() <= 0){
            try {
                BigInteger latest = Web3jHandler.getInstance(ethServerUrl).getLatestBlockNumber();
                if(endNum.compareTo(latest) <= 0 ){
                    return RestResp.fail("database not update");
                }
            } catch (IOException e) {
                log.error("Get latest block number Error 2");
                return RestResp.fail("Check Block Number Error");
            }
        }
        List<BigInteger> result = new ArrayList<>();
        for(;startNum.compareTo(endNum) <= 0; startNum = startNum.add(new BigInteger("1"))){
            if (!list.contains(startNum)) {
                result.add(startNum);
            }
        }
        if(result.size() > 0){
            return RestResp.success("Some blocks not sync: ", result);
        }else {
            return RestResp.success("Sync complete");
        }

    }

    public RestResp syncBlock(BigInteger startNum, BigInteger endNum){
        if(null == startNum || endNum ==null){
            return RestResp.fail("Sync Block Number Error");
        }
        if(startNum.compareTo(endNum) == 1){
            return RestResp.fail("start num grater than end num");
        }
        for(;startNum.compareTo(endNum) <= 0; startNum = startNum.add(new BigInteger("1"))){
            try {
               EthBlock.Block block = Web3jHandler.getInstance(ResParam.getEthBlockinfoServer()).getBlock(startNum);
               EthBlockInfo blockInfo = blockInfo = EthBlockInfo.block2BlockInfo(block);
               blockInfo = blockInfoRepo.save(blockInfo);
               saveTransaction(block);
            } catch (IOException e) {
                log.error("Sync Block Error: {} : {}", startNum, e);
                return RestResp.fail("Sync Block Error: "+ startNum,  e);
            }
        }
        return RestResp.success("Sync Block End...");
    }

    private void saveTransaction(EthBlock.Block block){
        List<EthBlock.TransactionResult> list = block.getTransactions();
        List<EthTransactionInfo> infos = null;
        if(null != list && list.size()>0){
            infos = new ArrayList<>(list.size());
            EthTransactionInfo info = null;
            for(EthBlock.TransactionResult result : list){
                String hash = (String) result.get();
                Transaction transaction = null;
                try {
                    transaction = Web3jHandler.getInstance(ResParam.getEthBlockinfoServer()).getTransactionByHash(hash);
                } catch (IOException e) {
                    log.error("Get Transaction by Hash Error: ", e);
                }
                if(null != transaction){
                    info = new EthTransactionInfo(transaction);
                    info.setTimestamp(block.getTimestamp() == null ? null : block.getTimestamp().longValue());
                    try {
                        TransactionReceipt receipt = Web3jHandler.getInstance(ResParam.getEthBlockinfoServer()).getTransactionReceipt(hash);
                        BigInteger gasUsed = receipt.getGasUsed();
                        info.setGasUsed(gasUsed == null ? null : gasUsed.toString());
                        info.setTxstatus(receipt.getStatus());
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
    @Resource
    private RedisService redisService;

    private List<EthBlockInfo> getBlockInfoFromRedis(){
        log.info("*** get block info from redis ***");
        Map<String,Object> map = null;
        try{
            map = redisService.getMap(Constants.POA_LATEST_BLOCK_10);
        }catch (Exception e){
            log.error("Get data from redis Error: ", e);
            return null;
        }
        if(null != map && map.size() >0){
            List<EthBlockInfo> result = new ArrayList<>(map.size());
            for(Map.Entry<String,Object > entry: map.entrySet()){
                EthBlockInfo info =(EthBlockInfo)  JsonUtil.fromJson(entry.getValue().toString(), EthBlockInfo.class);
                EthBlockInfoVO vo = new EthBlockInfoVO(info);
                result.add(info);
            }
            Collections.sort(result, new Comparator<EthBlockInfo>() {
                @Override
                public int compare(EthBlockInfo o1, EthBlockInfo o2) {
                    return  -o1.getNumber().compareTo(o2.getNumber());
                }
            });
            return result;
        }
        return null;
    }
    private List<EthBlockInfo> getBlockInfo(int total) throws IOException {
        log.info("*** get block info from blockchain ***");
        List<EthBlockInfo> result = new ArrayList<>(total);
        List<EthBlock.Block> list = Web3jHandler.getInstance(ethServerUrl).getLatestBlock(total);
        for(EthBlock.Block block : list){
            result.add(EthBlockInfo.block2BlockInfo(block));
        }
        Collections.sort(result, new Comparator<EthBlockInfo>() {
            @Override
            public int compare(EthBlockInfo o1, EthBlockInfo o2) {
                return  -o1.getNumber().compareTo(o2.getNumber());
            }
        });
        return result;
    }

    private EthTransactionInfo getTransactionInfo(String txhash) throws IOException {
            Transaction transaction = Web3jHandler.getInstance(ethServerUrl).getTransaction(txhash);
            EthBlock.Block block = Web3jHandler.getInstance(ethServerUrl).getBlock(transaction.getBlockNumber());
            BigInteger gasUsed = Web3jHandler.getInstance(ethServerUrl).getGasUsed(txhash);

            EthTransactionInfo info = new EthTransactionInfo(transaction, null == block.getTimestamp()?null: block.getTimestamp().longValue());
            info.setGasUsed(null ==gasUsed.toString() ? null : gasUsed.toString());
            return info;
    }

    private List<EthTransactionInfoVO> list2VOS(List<EthTransactionInfo> list){
        if(null != list && list.size() > 0){
            List<EthTransactionInfoVO> vos = new ArrayList<>(list.size());
            EthTransactionInfoVO vo = null;
            for(EthTransactionInfo info : list){
                vo = new EthTransactionInfoVO(info);
                vos.add(vo);
            }
            return vos;
        }
        return null;
    }

    private List<EthBlockInfoVO> listToVOS(List<EthBlockInfo> list){
        if(null != list && list.size() > 0){
            List<EthBlockInfoVO> vos = new ArrayList<>(list.size());
            EthBlockInfoVO vo = null;
            for(EthBlockInfo info : list){
                vo = new EthBlockInfoVO(info);
                vos.add(vo);
            }
            return vos;
        }
        return null;
    }
}
