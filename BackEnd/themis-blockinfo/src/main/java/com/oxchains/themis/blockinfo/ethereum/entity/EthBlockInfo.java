package com.oxchains.themis.blockinfo.ethereum.entity;

import com.oxchains.themis.common.ethereum.Web3jHandler;
import lombok.Data;
import org.web3j.protocol.core.methods.response.EthBlock;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.SignatureException;
import java.util.List;

/**
 * @author ccl
 * @time 2018-05-22 15:10
 * @name BlockInfo
 * @desc:
 */
@Data
@Entity
@Table(name = "eth_block_info")
public class EthBlockInfo{
    @Id
    @Column(columnDefinition ="bigint")
    private BigInteger number;

    private String hash;
    private String parentHash;
    @Column(columnDefinition ="bigint")
    private BigInteger nonce;
    private String sha3Uncles;
    @Column(length = 1024)
    private String logsBloom;
    private String transactionsRoot;
    private String stateRoot;
    private String receiptsRoot;
    private String author;
    private String miner;
    private String realMiner;
    private String mixHash;
    @Column(columnDefinition ="bigint")
    private BigInteger difficulty;
    @Column(columnDefinition ="bigint")
    private BigInteger totalDifficulty;
    @Column(length = 2048)
    private String extraData;
    private Integer size;
    @Column(columnDefinition ="bigint")
    private BigInteger gasLimit;
    private String gasUsed;
    private Long timestamp;
    private Integer txs;
    private Integer ucs;

    public static EthBlockInfo block2BlockInfo(EthBlock.Block block){
        EthBlockInfo blockInfo = new EthBlockInfo();
        blockInfo.setNumber(block.getNumber());
        blockInfo.setHash(block.getHash());
        blockInfo.setParentHash(block.getParentHash());
        blockInfo.setNonce(block.getNonce());
        blockInfo.setSha3Uncles(block.getSha3Uncles());
        blockInfo.setLogsBloom(block.getLogsBloom());
        blockInfo.setTransactionsRoot(block.getTransactionsRoot());
        blockInfo.setStateRoot(block.getStateRoot());
        blockInfo.setReceiptsRoot(block.getReceiptsRoot());
        blockInfo.setAuthor(block.getAuthor());
        blockInfo.setMiner(block.getMiner());
        blockInfo.setMixHash(block.getMixHash());
        blockInfo.setDifficulty(block.getDifficulty());
        blockInfo.setTotalDifficulty(block.getTotalDifficulty());
        blockInfo.setExtraData(block.getExtraData());
        blockInfo.setSize(block.getSize()==null?null:block.getSize().intValue());
        blockInfo.setGasLimit(block.getGasLimit());
        blockInfo.setGasUsed(block.getGasUsed() == null ? null : block.getGasUsed().toString());
        blockInfo.setTimestamp(block.getTimestamp()==null?null:block.getTimestamp().longValue()*1000);
        blockInfo.setTxs(null == block.getTransactions() ? 0 : block.getTransactions().size());
        blockInfo.setUcs(null == block.getUncles() ? 0 : block.getUncles().size());
        if(block.getNumber().longValue() == 0L){
            blockInfo.setRealMiner(block.getMiner());
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
            blockInfo.setRealMiner(rm);
        }
        return blockInfo;
    }

    public EthBlockInfo() {
    }
}
