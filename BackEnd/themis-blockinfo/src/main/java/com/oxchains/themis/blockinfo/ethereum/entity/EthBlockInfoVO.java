package com.oxchains.themis.blockinfo.ethereum.entity;

import com.oxchains.themis.common.ethereum.Web3jHandler;
import com.oxchains.themis.common.util.ArithmeticUtils;
import com.oxchains.themis.common.util.DateUtil;
import lombok.Data;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ccl
 * @time 2018-05-22 15:10
 * @name EthBlockInfoVO
 * @desc:
 */
@Data
public class EthBlockInfoVO extends  EthBlockInfo{

    private List<String> transactions;

    private List<String> uncles;

    private List<String> sealFields;

    private String age;

    public EthBlockInfoVO(EthBlock.Block block){
        this.setNumber(block.getNumber());
        this.setHash(block.getHash());
        this.setParentHash(block.getParentHash());
        this.setNonce(block.getNonce());
        this.setSha3Uncles(block.getSha3Uncles());
        this.setLogsBloom(block.getLogsBloom());
        this.setTransactionsRoot(block.getTransactionsRoot());
        this.setStateRoot(block.getStateRoot());
        this.setReceiptsRoot(block.getReceiptsRoot());
        this.setAuthor(block.getAuthor());
        this.setMiner(block.getMiner());
        this.setMixHash(block.getMixHash());
        this.setDifficulty(block.getDifficulty());
        this.setTotalDifficulty(block.getTotalDifficulty());
        this.setExtraData(block.getExtraData());
        this.setSize(block.getSize()==null?null:block.getSize().intValue());
        this.setGasLimit(block.getGasLimit());
        this.setGasUsed(block.getGasUsed() == null ? null : block.getGasUsed().toString());
        this.setTimestamp(block.getTimestamp()==null?null:block.getTimestamp().longValue()*1000);
        this.setTxs(null == block.getTransactions() ? 0 : block.getTransactions().size());
        this.setUcs(null == block.getUncles() ? 0 : block.getUncles().size());
        List<String> trans = new ArrayList<>(block.getTransactions().size());
        block.getTransactions().stream().forEach(transactionResult -> {
            trans.add(transactionResult.get().toString());
        });
        this.setTransactions(trans);
        this.setUncles(block.getUncles());
        if(block.getNumber().longValue() == 0L){
            this.setRealMiner(block.getMiner());
        }else{
            /**
             * 解析realminer
             */
            String rm = null;
            try {
                rm = Web3jHandler.parseAddressFromExtra(block);
            } catch (SignatureException e) {
                e.printStackTrace();
            }
            this.setRealMiner(rm);
        }
        this.setAge(DateUtil.getDiffTimeString( null != block.getTimestamp()?null: block.getTimestamp().longValue(), 1000));
    }

    public EthBlockInfoVO(EthBlockInfo block){
        this.setNumber(block.getNumber());
        this.setHash(block.getHash());
        this.setParentHash(block.getParentHash());
        this.setNonce(block.getNonce());
        this.setSha3Uncles(block.getSha3Uncles());
        this.setLogsBloom(block.getLogsBloom());
        this.setTransactionsRoot(block.getTransactionsRoot());
        this.setStateRoot(block.getStateRoot());
        this.setReceiptsRoot(block.getReceiptsRoot());
        this.setAuthor(block.getAuthor());
        this.setMiner(block.getMiner());
        this.setRealMiner(block.getRealMiner());
        this.setMixHash(block.getMixHash());
        this.setDifficulty(block.getDifficulty());
        this.setTotalDifficulty(block.getTotalDifficulty());
        this.setExtraData(block.getExtraData());
        this.setSize(block.getSize()==null?null:block.getSize().intValue());
        this.setGasLimit(block.getGasLimit());
        //String val = ArithmeticUtils.convertToEther(block.getGasUsed() == null ? "0" : block.getGasUsed().toString());
        this.setGasUsed(block.getGasUsed());
        this.setTimestamp(block.getTimestamp()==null?null:block.getTimestamp().longValue());
        this.setTxs(block.getTxs());
        this.setUcs(block.getUcs());
        this.setAge(DateUtil.getDiffTimeString(block.getTimestamp(), 1000));
    }

    public static EthBlockInfoVO do2VO(EthBlock.Block block){
        if(null == block){
            return null;
        }
        EthBlockInfoVO vo = new EthBlockInfoVO(block);
        return vo;
    }
    public EthBlockInfoVO() {
    }
}
