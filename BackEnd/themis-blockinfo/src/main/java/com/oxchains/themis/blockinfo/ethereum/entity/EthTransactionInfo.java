package com.oxchains.themis.blockinfo.ethereum.entity;

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
@Entity
@Table(name = "eth_transaction_info")
public class EthTransactionInfo {

    @Id
    private String thash;
    @Column(columnDefinition ="bigint")
    private BigInteger nonce;
    private String blockHash;
    @Column(columnDefinition ="bigint")
    private BigInteger blockNumber;
    @Column(columnDefinition ="bigint")
    private BigInteger transactionIndex;
    private String fromAddr;
    private String toAddr;
    private String value;
    private String gasPrice;
    private String gas;
    @Column(length = 10240)
    private String input;
    private String creates;
    private String publicKey;
    private String raw;
    private String r;
    private String s;
    private int v;
    private Long timestamp;
    private String gasUsed;
    private String txstatus;
    public EthTransactionInfo() {
    }
    public EthTransactionInfo(Transaction transaction){
        this(transaction, null);
    }

    public EthTransactionInfo(Transaction transaction, Long timestamp){
        if(null == transaction){
            return;
        }
        this.thash = transaction.getHash();
        this.nonce = transaction.getNonce();
        this.blockHash = transaction.getBlockHash();
        this.blockNumber = transaction.getBlockNumber();
        this.transactionIndex = transaction.getTransactionIndex();
        this.fromAddr = transaction.getFrom();
        this.toAddr = transaction.getTo();
        this.value = null == transaction.getValue() ? null : transaction.getValue().toString();
        this.gasPrice = null == transaction.getGasPrice() ? null : transaction.getGasPrice().toString();
        this.gas = null == transaction.getGas() ? null : transaction.getGas().toString();
        this.input = transaction.getInput();
        this.creates = transaction.getCreates();
        this.publicKey = transaction.getPublicKey();
        //this.raw = transaction.getRaw();
        this.r = transaction.getR();
        this.s = transaction.getS();
        this.v = transaction.getV();

        this.timestamp = timestamp;
    }




}
