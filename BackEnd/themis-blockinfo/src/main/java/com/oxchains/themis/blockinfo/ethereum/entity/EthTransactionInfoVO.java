package com.oxchains.themis.blockinfo.ethereum.entity;

import com.oxchains.themis.common.util.ArithmeticUtils;
import com.oxchains.themis.common.util.DateUtil;
import lombok.Data;
import org.web3j.protocol.core.methods.response.Transaction;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * @author ccl
 * @time 2018-06-28 16:05
 * @name EthTransaction
 * @desc:
 */

@Data
public class EthTransactionInfoVO extends EthTransactionInfo{

    private String age;
    private String txfee;

    public EthTransactionInfoVO() {
    }

    public EthTransactionInfoVO(EthTransactionInfo transaction){
        if(null == transaction){
            return;
        }
        this.setThash(transaction.getThash());
        this.setNonce(transaction.getNonce());
        this.setBlockHash(transaction.getBlockHash());
        this.setBlockNumber(transaction.getBlockNumber());
        this.setTransactionIndex(transaction.getTransactionIndex());
        this.setFromAddr(transaction.getFromAddr());
        this.setToAddr(transaction.getToAddr());
        this.setValue(ArithmeticUtils.convertToEther(transaction.getValue()));
        String gasPrice = transaction.getGasPrice();
        this.setGasPrice(ArithmeticUtils.convertToEther(gasPrice));
        this.setGas(transaction.getGas());
        this.setInput(transaction.getInput());
        this.setCreates(transaction.getCreates());
        this.setPublicKey(transaction.getPublicKey());
        this.setRaw(transaction.getRaw());
        this.setR( transaction.getR());
        this.setS(transaction.getS());
        this.setV(transaction.getV());
        String gasUsed = transaction.getGasUsed();
        this.setGasUsed(gasUsed);
        this.setTimestamp(transaction.getTimestamp());
        this.setAge(DateUtil.getDiffTimeString(transaction.getTimestamp(), 1000));

        this.setTxfee(ArithmeticUtils.convertToEther(ArithmeticUtils.multiply(gasPrice, gasUsed)));

        this.setTxstatus(transaction.getTxstatus());

    }




}
