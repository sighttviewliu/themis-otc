package com.themis.blockinfo.ethereum.entity;

import lombok.Data;
import org.web3j.protocol.core.methods.response.EthBlock;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;

/**
 * @author ccl
 * @time 2018-05-22 15:10
 * @name BlockInfo
 * @desc:
 */
@Data
@Entity
@Table(name = "block_info")
public class BlockInfo{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigInteger number;
    private String hash;
    private String parentHash;
    private BigInteger nonce;
    private String sha3Uncles;
    @Column(length = 1024)
    private String logsBloom;
    private String transactionsRoot;
    private String stateRoot;
    private String receiptsRoot;
    private String author;
    private String miner;
    private String mixHash;
    private BigInteger difficulty;
    private BigInteger totalDifficulty;
    private String extraData;
    private Integer size;
    private BigInteger gasLimit;
    private BigInteger gasUsed;
    private Long timestamp;
    @Transient
    private List<EthBlock.TransactionResult> transactions;
    @Transient
    private List<String> uncles;
    @Transient
    private List<String> sealFields;

    public static BlockInfo block2BlockInfo(EthBlock.Block block){
        BlockInfo blockInfo = new BlockInfo();
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
        blockInfo.setGasUsed(block.getGasUsed());
        blockInfo.setTimestamp(block.getTimestamp()==null?null:block.getTimestamp().longValue());

        return blockInfo;
    }

    public BlockInfo() {
    }
}
