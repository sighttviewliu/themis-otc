package com.oxchains.themis.repo.entity.order;

import lombok.Data;

import javax.persistence.*;

/**
 * 上报秘钥碎片验证错误信息表
 * @author anonymity
 * @create 2018-08-15 11:12
 **/
@Data
@Entity
@Table(name = "proof_error")
public class ProofError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String orderId;
    private String error;
    private Long createTime;
    private boolean push; // 出现错误

    public ProofError() {
    }

    public ProofError(String orderId, Long userId, String error, Long createTime) {
        this.orderId = orderId;
        this.userId = userId;
        this.error = error;
        this.createTime = createTime;
    }
}
