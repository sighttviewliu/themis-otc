package com.oxchains.themis.repo.entity.order;

import lombok.Data;

import javax.persistence.*;

/**
 * 保存转账的交易hash
 * @author anonymity
 * @create 2018-08-23 16:52
 **/
@Data
@Entity
@Table(name = "trustee_transaction_hash")
public class TrusteeTransactionHash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderId;
    private String txHash;
    private Long createTime;

    public TrusteeTransactionHash() {
    }

    public TrusteeTransactionHash(String orderId, String txHash, Long createTime) {
        this.orderId = orderId;
        this.txHash = txHash;
        this.createTime = createTime;
    }
}
