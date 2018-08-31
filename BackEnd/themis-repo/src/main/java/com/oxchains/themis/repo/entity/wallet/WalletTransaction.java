package com.oxchains.themis.repo.entity.wallet;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author ccl
 * @time 2018-06-11 18:00
 * @name WalletTransaction
 * @desc:
 */
@Data
@Entity
@Table(name = "user_wallet_transaction")
public class WalletTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long userId;

    private String fromAddr;
    private String toAddr;
    private Double amount;
    private String hash;
    private Long blockNumber;

    public WalletTransaction() {
    }

    public WalletTransaction(Long createTime, Long userId, String fromAddr, String toAddr, Double amount, String hash) {
        this.createTime = createTime;
        this.userId = userId;
        this.fromAddr = fromAddr;
        this.toAddr = toAddr;
        this.amount = amount;
        this.hash = hash;
    }
}
